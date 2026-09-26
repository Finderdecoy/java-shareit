package ru.practicum.shareit.item.itemDto;

import lombok.Data;

@Data
public class ItemDtoOnCreate {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
