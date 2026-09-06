package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.StatusException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.commentDto.CommentInDto;
import ru.practicum.shareit.item.commentDto.CommentOutDto;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemDtoWithDate;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMap;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepo;
import ru.practicum.shareit.item.repository.ItemRepo;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepo itemRepo;
    private final UserRepository userRepository;
    private final BookingRepo bookingRepo;
    private final CommentRepo commentRepo;

    @Override
    public ItemDto createItem(Long idUser, Item item) {
        User owner = checkUser(idUser);
        log.info("Пользователь : {} .Добовляет вешь {}", owner, item);
        item.setOwner(owner);
        return ItemMap.mapToDto(itemRepo.save(item));
    }

    @Override
    public ItemDtoWithDate getItem(Long idItem, Long idUser) {
        log.info("Запрос вещи по id {}", idItem);
        Item item = itemRepo.findById(idItem)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (!item.getOwner().getId().equals(idUser) && !item.getAvailable()) {
            throw new StatusException("Вещь сейчас не доступна");
        }
        List<CommentOutDto> comments = commentRepo.findByItemId(idItem).stream()
                .map(CommentMapper::mapToOutDto)
                .toList();
        if (item.getOwner().getId().equals(idUser)) {
            List<Booking> bookings = bookingRepo.findByItemBookingIdInAndStatus(List.of(idItem),BookingStatus.APPROVED);
            ItemDtoWithDate dto = getItemWithDate(item, bookings);
            dto.setComments(comments);
            return dto;
        }
        return ItemMap.mapToDtoWithOutDate(item).toBuilder().comments(comments).build();
    }

    private ItemDtoWithDate getItemWithDate(Item item, List<Booking> bookings) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastBookingDate = bookings.stream()
                .map(Booking::getBookingEndDate)
                .filter(end -> end.isBefore(now))
                .max(LocalDateTime::compareTo)
                .orElse(null);
        LocalDateTime nextBooking = bookings.stream()
                .map(Booking::getBookingStartDate)
                .filter(start -> start.isAfter(now))
                .min(LocalDateTime::compareTo)
                .orElse(null);
        return ItemMap.mapToDtoWithDate(item, lastBookingDate, nextBooking);
    }

    @Override
    public Collection<ItemDtoWithDate> getItemList(Long idUser) {
        userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы."));
        List<Item> items = itemRepo.findByOwnerId(idUser);
        if (items.isEmpty()) return List.of();
        List<Long> itemIds = items.stream().map(Item::getId).toList();
        Map<Long, List<Booking>> bookingsMap = bookingRepo.findByItemBookingIdInAndStatus(itemIds, BookingStatus.APPROVED)
                .stream()
                .collect(Collectors.groupingBy(b -> b.getItemBooking().getId()));
        List<ItemDtoWithDate> itemsDto = items.stream()
                .map(item -> {
                    List<Booking> bookings = bookingsMap.getOrDefault(item.getId(), List.of());
                    return getItemWithDate(item, bookings);
                })
                .toList();
        Map<Long, List<Comment>> itemWithComments = commentRepo.findByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(comment -> (long) comment.getItem().getId()));
        itemsDto.forEach(i -> i.setComments(
                itemWithComments.getOrDefault(i.getId(), List.of()).stream()
                        .map(CommentMapper::mapToOutDto)
                        .toList()
        ));
        return itemsDto;
    }


    @Override
    public ItemDto editItem(Long idUser, Long id, Item item) {
        log.info("Пришел запрос от пользователя id: {}. На изменение вещи {}", idUser, id);
        User owner = checkUser(idUser);
        log.info("Пользователь с id {} Найден : {}", idUser, owner);
        Item editingItem = itemRepo.findById(id).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (editingItem.getOwner().equals(owner)) {
            if (item.getAvailable() != null) editingItem.setAvailable(item.getAvailable());
            if (item.getDescription() != null && !item.getDescription().isBlank())
                editingItem.setDescription(item.getDescription());
            if (item.getName() != null && !item.getName().isBlank()) editingItem.setName(item.getName());

            return ItemMap.mapToDto(itemRepo.save(editingItem));
        }
        throw new NotFoundException("Вы не являетесь владельцем данной вещи");
    }

    @Override
    public Collection<ItemDto> searchAvailableItems(String searchQuery) {
        log.info("Запрос вещи по названию или описанию : {}", searchQuery);
        searchQuery = '%' + searchQuery.toLowerCase() + '%';
        return itemRepo.findByDescriptionOrName(searchQuery).stream().map(ItemMap::mapToDto).toList();
    }

    @Override
    public CommentOutDto setComment(Long itemId, Long userId, CommentInDto commentDto) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User user = checkUser(userId);
        if (bookingRepo.existsByItemBookingIdAndBookerIdAndStatusAndBookingEndDateBefore(itemId, userId,
                BookingStatus.APPROVED, LocalDateTime.now())) {
            Comment comment = commentRepo.save(CommentMapper.mapToModel(commentDto.getText(), item, user));
            return CommentMapper.mapToOutDto(comment);
        }
        throw new ValidateException("Вы не можете добавить коментарий, так как еще не арендовывали эту вещь");
    }

    private User checkUser(Long idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public Item findById(Long itemId) {
        return itemRepo.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }
}
