package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemDtoOnCreate;
import ru.practicum.shareit.item.itemDto.ItemDtoWithComment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMap {
    public static ItemDto mapToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemDtoWithComment mapToDtoWithComments(Item item) {
        return ItemDtoWithComment.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(List.of())
                .build();
    }

    public static Item mapToItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static Item mapToItemOnCreate(ItemDtoOnCreate itemCreate) {
        return Item.builder()
                .name(itemCreate.getName())
                .description(itemCreate.getDescription())
                .available(itemCreate.getAvailable())
                .build();
    }
}
