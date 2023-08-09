package ru.practicum.ewm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(ContentNotFountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleException(ContentNotFountException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный запрос.",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ContentAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleException(ContentAlreadyExistException exception) {
        return new ApiError(exception.getMessage(),
                "Ограниечение целостности нарушено.",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(EditingNotAllowedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleException(EditingNotAllowedException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный запрос.",
                HttpStatus.FORBIDDEN.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(NumberFormatException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный запрос.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(UnknownStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleException(UnknownStateException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный запрос.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ApiError> handleException(ConstraintViolationException exception) {
        List<ApiError> errors = new ArrayList<>();
        exception.getConstraintViolations().forEach(e -> {
            String field = e.getPropertyPath().toString();
            String message = e.getMessage();
            ApiError apiError = new ApiError(field + ": " + message,
                    "Некорректный запрос.",
                    HttpStatus.BAD_REQUEST.toString(),
                    LocalDateTime.now());
            errors.add(apiError);
        });
        return errors;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ApiError> handleException(MethodArgumentNotValidException exception) {
        List<ApiError> errors = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach(e -> {
            String field = ((FieldError) e).getField();
            String message = e.getDefaultMessage();
            ApiError apiError = new ApiError(field + ": " + message,
                    "Некорректный запрос.",
                    HttpStatus.BAD_REQUEST.toString(),
                    LocalDateTime.now());
            errors.add(apiError);
        });
        return errors;
    }
}
