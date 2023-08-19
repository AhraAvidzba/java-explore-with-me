package ru.practicum.statistics.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(NotValidDatesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(NotValidDatesException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный запрос.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }
}
