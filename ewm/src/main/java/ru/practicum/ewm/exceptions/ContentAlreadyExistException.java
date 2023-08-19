package ru.practicum.ewm.exceptions;

public class ContentAlreadyExistException extends RuntimeException {
    public ContentAlreadyExistException(String message) {
        super(message);
    }
}
