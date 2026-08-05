package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

@Data
public class Booking {
    @NotNull
    private Long id;
    @NotNull
    private Item itemBooking;
    @NotNull
    private LocalDate bookingStartDate;
    @NotNull
    private LocalDate bookingEndDate;
    private Boolean bookingConfirmation;
}
