package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(Long idUser, Item item);

    ItemDto getItem(Long idItem);

    Collection<ItemDto> getItemList(Long idUser);

    ItemDto editItem(Long idUser, Long idItem, Item item);

    Collection<ItemDto> searchAvailableItems(String searchQuery);
}
