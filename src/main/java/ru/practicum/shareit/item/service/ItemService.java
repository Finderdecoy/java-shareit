package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.commentDto.CommentInDto;
import ru.practicum.shareit.item.commentDto.CommentOutDto;
import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    ItemDto createItem(Long idUser, Item item);

    ItemDtoWithDate getItem(Long idItem);

    Collection<ItemDtoWithDate> getItemList(Long idUser);

    ItemDto editItem(Long idUser, Long idItem, Item item);

    Collection<ItemDto> searchAvailableItems(String searchQuery);

    CommentOutDto setComment(Long itemId, Long userId, CommentInDto dto);
}
