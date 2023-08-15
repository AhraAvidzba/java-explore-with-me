package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventService {
    //Приватные методы
    List<EventShortDto> getUserEvents(int from, int size, Long userId);

    EventOutDto addEvent(EventInDto eventInDto, Long userId);

    EventOutDto getFullUserEvent(Long eventId, Long userId);

    EventOutDto editEvent(UpdateEventRequestDto event, Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsForUserEvent(Long eventId, Long userId);

    EventRequestStatusUpdateResult changeStatusForUserEventsRequests(EventRequestStatusUpdateRequest requestsAndStatus, Long eventId, Long userId);

    //Публичные методы
    List<EventShortDto> getEvents(PublicGetEventsCriteria publicGetEventsCriteria);

    EventOutDto getFullEventById(Long eventId);

    //Методы для админов
    List<EventOutDto> getFullEvents(AdminGetEventsCriteria adminGetEventsCriteria);

    EventOutDto editEventByAdmin(UpdateEventRequestDto event, Long eventId);
}
