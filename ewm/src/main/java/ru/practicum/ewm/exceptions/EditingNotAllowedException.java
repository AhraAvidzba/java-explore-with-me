package ru.practicum.ewm.exceptions;

public class EditingNotAllowedException extends RuntimeException {
    public EditingNotAllowedException(String message) {
        super(message);
    }
}
