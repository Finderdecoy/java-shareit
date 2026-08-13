package ru.practicum.shareit.item.itemDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.commentDto.CommentOutDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemDtoWithDate {
    @NotEmpty
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentOutDto> comments;
}
