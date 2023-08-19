package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminGetEventsCriteria;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.UpdateEventRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public List<EventOutDto> getEvents(@Valid AdminGetEventsCriteria adminGetEventsCriteria,
                                       HttpServletRequest request) {
        List<EventOutDto> eventShortsDto = eventService.getFullEvents(adminGetEventsCriteria);
        log.info("Возвращается список с полной информацией о событии. Эндпоинт {}", request.getRequestURI());
        return eventShortsDto;
    }

    @PatchMapping("/{eventId}")
    public EventOutDto editEvent(@RequestBody UpdateEventRequestDto eventDto,
                                 @PathVariable Long eventId,
                                 HttpServletRequest request) {
        EventOutDto eventOutDto = eventService.editEventByAdmin(eventDto, eventId);
        log.info("Событие изменено. Эндпоинт {}", request.getRequestURI());
        return eventOutDto;
    }

}
