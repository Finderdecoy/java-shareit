package ru.practicum.shareit.item.itemDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDtoOnCreate {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
