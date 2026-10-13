package ru.practicum.shareit.exception;

public class ConflictData extends RuntimeException {
    public ConflictData(String message) {
        super(message);
    }
}
