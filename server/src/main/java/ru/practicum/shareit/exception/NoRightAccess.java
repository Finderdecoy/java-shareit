package ru.practicum.shareit.exception;

public class NoRightAccess extends RuntimeException {
    public NoRightAccess(String message) {
        super(message);
    }
}
