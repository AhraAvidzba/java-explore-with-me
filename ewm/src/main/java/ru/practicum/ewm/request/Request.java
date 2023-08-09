package ru.practicum.ewm.request;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

public class Request {
    private Long id;
    private LocalDateTime created;
    private Event event;
    private User Requester;
    private Status status;
}
