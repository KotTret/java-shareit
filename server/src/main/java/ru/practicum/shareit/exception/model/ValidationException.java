package ru.practicum.shareit.exception.model;

public class ValidationException extends RuntimeException {

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
