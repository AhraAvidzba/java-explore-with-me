package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
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
                                             @PathVariable Long userId,
                                             HttpServletRequest request) {
        List<EventShortDto> eventShortsDto = eventService.getUserEvents(from, size, userId);
        log.info("Возвращается список с краткой информацией о событии. Эндпоинт {}", request.getRequestURI());
        return eventShortsDto;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventOutDto addEvent(@Valid @RequestBody EventInDto eventInDto,
                                @PathVariable Long userId,
                                HttpServletRequest request) {
        EventOutDto eventOutDto = eventService.addEvent(eventInDto, userId);
        log.info("Событие добавлено. Эндпоинт {}", request.getRequestURI());
        return eventOutDto;
    }

    @GetMapping("/{eventId}")
    public EventOutDto getFullUserEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        HttpServletRequest request) {
        EventOutDto eventOutDto = eventService.getFullUserEvent(eventId, userId);
        log.info("Возвращается полная информация о событии. Эндпоинт {}", request.getRequestURI());
        return eventOutDto;
    }

    @PatchMapping("/{eventId}")
    public EventOutDto editEvent(@Valid @RequestBody UpdateEventRequestDto eventDto,
                                 @PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 HttpServletRequest request) {
        EventOutDto eventOutDto = eventService.editEvent(eventDto, userId, eventId);
        log.info("Событие изменено. Эндпоинт {}", request.getRequestURI());
        return eventOutDto;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsForUserEvent(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 HttpServletRequest request) {
        List<ParticipationRequestDto> participationRequestDto = eventService.getRequestsForUserEvent(eventId, userId);
        log.info("Возвращается все запросы на участие для запрашиваемого события. Эндпоинт {}", request.getRequestURI());
        return participationRequestDto;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatusForUserEventsRequests(@Valid @RequestBody EventRequestStatusUpdateRequest requestsAndStatus,
                                                                            @PathVariable Long userId,
                                                                            @PathVariable Long eventId,
                                                                            HttpServletRequest request) {
        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = eventService.changeStatusForUserEventsRequests(requestsAndStatus, eventId, userId);
        log.info("Статус запросов на участие в событии изменен. Эндпоинт {}", request.getRequestURI());
        return eventRequestStatusUpdateResult;
    }
}
