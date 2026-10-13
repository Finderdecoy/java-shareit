package ru.practicum.shareit.item.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class ItemDtoOnCreate {
    @NotBlank
    private String name;
    @NotBlank
    @Length(max = 500)
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
}
