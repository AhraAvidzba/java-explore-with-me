package ru.practicum.ewm.exceptions;

public class UnavailableOperationException extends RuntimeException {
    public UnavailableOperationException(String message) {
        super(message);
    }
}
