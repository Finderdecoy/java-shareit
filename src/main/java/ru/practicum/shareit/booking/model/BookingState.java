package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.ValidateException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState fromString(String state) {
        return switch (state.toLowerCase()) {
            case "all" -> BookingState.ALL;
            case "current" -> BookingState.CURRENT;
            case "past" -> BookingState.PAST;
            case "future" -> BookingState.FUTURE;
            case "waiting" -> BookingState.WAITING;
            case "rejected" -> BookingState.REJECTED;
            default -> throw new ValidateException("Unexpected value: " + state.toLowerCase());
        };
    }
}
