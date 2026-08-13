package ru.practicum.shareit.item.itemDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.commentDto.CommentOutDto;

import java.util.List;

@Data
@Builder
public class ItemDtoWithComment {
    @NotEmpty
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private List<CommentOutDto> comments;
}
