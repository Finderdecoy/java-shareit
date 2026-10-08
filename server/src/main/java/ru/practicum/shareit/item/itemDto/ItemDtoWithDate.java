package ru.practicum.shareit.item.itemDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.commentDto.CommentOutDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class ItemDtoWithDate {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentOutDto> comments;
}
