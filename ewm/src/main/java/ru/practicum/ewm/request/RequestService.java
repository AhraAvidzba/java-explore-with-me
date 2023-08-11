package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    //Приватные методы
    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto addRequest(Long eventId, Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
