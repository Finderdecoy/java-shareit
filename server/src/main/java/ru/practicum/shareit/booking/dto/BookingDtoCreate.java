package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoCreate {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
}
