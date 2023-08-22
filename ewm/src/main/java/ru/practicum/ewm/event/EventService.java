package ru.practicum.ewm.event;

import ru.practicum.ewm.dtos.StatisticInDto;
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

    List<EventOutDto> getFriendsEventVisits(Long userId, Long friendId, int from, int size);

    List<EventOutDto> getFriendsEventPublishes(Long userId, Long friendId, int from, int size);


    //Публичные методы
    List<EventShortDto> getEvents(PublicGetEventsCriteria publicGetEventsCriteria, StatisticInDto statisticInDto);

    EventOutDto getFullEventById(Long eventId, StatisticInDto statisticInDto);

    //Методы для админов
    List<EventOutDto> getFullEvents(AdminGetEventsCriteria adminGetEventsCriteria);

    EventOutDto editEventByAdmin(UpdateEventRequestDto event, Long eventId);
}
