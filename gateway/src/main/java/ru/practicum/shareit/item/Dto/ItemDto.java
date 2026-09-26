package ru.practicum.shareit.item.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    @NotNull
    private String name;
    private String description;
    @NotNull
    private Boolean available;
}
