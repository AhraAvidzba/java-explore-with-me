package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.Request;
import ru.practicum.ewm.user.User;

public class RequestMapper {
    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .id(request.getId())
                .build();
    }

    public static Request mapToRequest(RequestDto requestDto, User requester, Event event) {
        return Request.builder()
                .requester(requester)
                .event(event)
                .created(requestDto.getCreated())
                .status(requestDto.getStatus())
                .id(requestDto.getId())
                .build();
    }
}
