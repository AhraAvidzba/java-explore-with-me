package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                             @Positive @RequestParam(defaultValue = "10") int size,
                                             @PathVariable Long userId) {
        List<EventShortDto> eventShortsDto = eventService.getUserEvents(from, size, userId);
        log.info("Возвращается список с краткой информацией о событии");
        return eventShortsDto;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventOutDto addEvent(@Valid @RequestBody EventInDto eventInDto,
                                @PathVariable Long userId) {
        EventOutDto eventOutDto = eventService.addEvent(eventInDto, userId);
        log.info("Событие добавлено");
        return eventOutDto;
    }

    @GetMapping("/{eventId}")
    public EventOutDto getFullUserEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        EventOutDto eventOutDto = eventService.getFullUserEvent(eventId, userId);
        log.info("Возвращается полная информация о событии");
        return eventOutDto;
    }

    @PatchMapping("/{eventId}")
    public EventOutDto editEvent(@Valid @RequestBody UpdateEventRequestDto eventDto,
                                 @PathVariable Long userId,
                                 @PathVariable Long eventId) {
        EventOutDto eventOutDto = eventService.editEvent(eventDto, userId, eventId);
        log.info("Событие изменено");
        return eventOutDto;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForUserEvent(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        List<ParticipationRequestDto> participationRequestDto = eventService.getRequestsForUserEvent(eventId, userId);
        log.info("Возвращается все запросы на участие для запрашиваемого события");
        return participationRequestDto;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatusForUserEventsRequests(@Valid @RequestBody EventRequestStatusUpdateRequest requestsAndStatus,
                                                                            @PathVariable Long userId,
                                                                            @PathVariable Long eventId) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = eventService.changeStatusForUserEventsRequests(requestsAndStatus, eventId, userId);
        log.info("Статус запросов на участие в событии изменен");
        return eventRequestStatusUpdateResult;
    }
}
