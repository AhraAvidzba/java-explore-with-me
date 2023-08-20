package ru.practicum.ewm.request.dto;

import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.ParticipationRequest;
import ru.practicum.ewm.user.User;

public class RequestMapper {
    public static ParticipationRequestDto mapToRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .created(participationRequest.getCreated())
                .status(participationRequest.getStatus())
                .id(participationRequest.getId())
                .showToEventSubscribers(participationRequest.getShowToEventSubscribers())
                .build();
    }

    public static ParticipationRequest mapToRequest(ParticipationRequestDto participationRequestDto, User requester, Event event) {
        return ParticipationRequest.builder()
                .requester(requester)
                .event(event)
                .created(participationRequestDto.getCreated())
                .status(participationRequestDto.getStatus())
                .id(participationRequestDto.getId())
                .build();
    }
}
