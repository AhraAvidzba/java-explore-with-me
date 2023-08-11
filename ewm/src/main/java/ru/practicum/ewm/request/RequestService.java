package ru.practicum.ewm.request;

import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    //Приватные методы
    List<RequestDto> getRequests(Long userId);
    RequestDto addRequest(Long eventId, Long userId);
    RequestDto cancelRequest(Long userId, Long requestId);
}
