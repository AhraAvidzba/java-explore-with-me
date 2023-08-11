package ru.practicum.ewm.exceptions;

public class ContentNotFoundException extends RuntimeException {
    public ContentNotFoundException(String message) {
        super(message);
    }
}
