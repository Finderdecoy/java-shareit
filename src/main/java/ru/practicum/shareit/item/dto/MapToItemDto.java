package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class MapToItemDto {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        //itemDto.setAvailable(item.getAvailable() ? "Доступная для аренды" : "Не доступна для аренды");
        itemDto.setAvailable(item.getAvailable());
        itemDto.setReviews(item.getReviews());
        return itemDto;
    }
}
