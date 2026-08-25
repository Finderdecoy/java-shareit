package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.itemDto.ItemDto;
import ru.practicum.shareit.item.itemDto.ItemDtoOnCreate;
import ru.practicum.shareit.item.itemDto.ItemDtoWithDate;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ItemMap {
    public static ItemDto mapToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
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

    public static ItemDtoWithDate mapToDtoWithOutDate(Item item) {
        return ItemDtoWithDate.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .lastBooking(null)
                .nextBooking(null)
                .available(item.getAvailable())
                .comments(new ArrayList<>())
                .build();
    }

    public static ItemDtoWithDate mapToDtoWithDate(Item item, LocalDateTime lastBooking, LocalDateTime nextBooking) {
        return ItemDtoWithDate.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .available(item.getAvailable())
                .comments(new ArrayList<>())
                .build();
    }
}
