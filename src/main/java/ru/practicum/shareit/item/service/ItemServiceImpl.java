package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repo.BookingRepo;
import ru.practicum.shareit.exception.NotFoundException;
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
    public ItemDtoWithDate getItem(Long idItem) {
        log.info("Запрос вещи по id {}", idItem);
        Item item = itemRepo.findById(idItem).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        List<CommentOutDto> comments = commentRepo.findByItemId(idItem).stream().map(CommentMapper::mapToOutDto).toList();
        if (item.getOwner().getId().equals(idItem))
            return itemRepo.findItemWithDates(idItem).toBuilder().comments(comments).build();
        return ItemMap.mapToDtoWithDate(item).toBuilder().comments(comments).build();
    }

    @Override
    public Collection<ItemDtoWithDate> getItemList(Long idUser) {
        userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Вы не зарегистрированы."));
        List<ItemDtoWithDate> items = itemRepo.findAllWithBookingDates(idUser);
        log.info("Подготовил список с комментариями и датами - {}", items);
        if (items.isEmpty()) return List.of();
        Collection<Long> itemsId = items.stream().map(ItemDtoWithDate::getId).toList();
        Map<Long, List<Comment>> itemWithComments = commentRepo.findByItemIdIn(itemsId).stream().collect(Collectors.groupingBy(comment -> (long) comment.getItem().getId()));
        items.forEach(i -> i.setComments(itemWithComments.getOrDefault(i.getId(), List.of()).stream().map(CommentMapper::mapToOutDto).toList()));
        return items;
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
        if (!bookingRepo.findByItemBookingIdAndBookerIdAndStatusAndBookingEndDateBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            Comment comment = commentRepo.save(CommentMapper.mapToModel(commentDto.getText(), item, user));
            return CommentMapper.mapToOutDto(comment);
        }
        throw new ValidateException("Вы не можете добавить коментарий, так как еще не арендовывали эту вещь");
    }

    private User checkUser(Long idUser) {
        return userRepository.findById(idUser).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
