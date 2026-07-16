package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.MapToItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepo;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepo itemRepo;

    @Override
    public ItemDto createItem(Long idUser, Item item) {
        User owner = checkUser(idUser);
        log.info("Пользователь : {} .Добовляет вешь {}", owner, item);
        return MapToItemDto.mapToItemDto(itemRepo.createItem(owner, item));
    }

    @Override
    public ItemDto getItem(Long idItem) {
        log.info("Запрос вещи по id {}", idItem);
        return itemRepo.getItemById(idItem)
                .map(MapToItemDto::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    @Override
    public Collection<ItemDto> getItemList(Long idUser) {
        return itemRepo.getAllItems(idUser).stream()
                .map(MapToItemDto::mapToItemDto)
                .toList();
    }

    @Override
    public ItemDto editItem(Long idUser, Long id, Item item) {
        log.info("Пришел запрос от пользователя id: {}. На изменение вещи {}", idUser, id);
        User owner = checkUser(idUser);
        log.info("Пользователь с id {} Найден : {}", idUser, owner);
        Item editingItem = itemRepo.getItemById(id).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (editingItem.getOwner().equals(owner)) {
            if (item.getAvailable() != null) editingItem.setAvailable(item.getAvailable());
            if (item.getDescription() != null && !item.getDescription().isBlank())
                editingItem.setDescription(item.getDescription());
            if (item.getName() != null && !item.getName().isBlank()) editingItem.setName(item.getName());

            return MapToItemDto.mapToItemDto(itemRepo.editItem(editingItem));
        }
        throw new NotFoundException("Вы не являетесь владельцем данной вещи");
    }

    @Override
    public Collection<ItemDto> searchAvailableItems(String searchQuery) {
        log.info("Запрос вещи по названи или описани : {}", searchQuery);
        if(searchQuery.isBlank()) return List.of();
        return itemRepo.searchAvailableItems(searchQuery).stream()
                .map(MapToItemDto::mapToItemDto)
                .toList();
    }

    private User checkUser(Long idUser) {
        return userRepository.getUser(idUser)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
