package ru.practicum.shareit.item.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ItemDtoOnCreate {
    @NotNull
    @NotEmpty
    private String name;
    @Length(max = 500)
    private String description;
    private Boolean available = true;
    private Long requestId;
}
