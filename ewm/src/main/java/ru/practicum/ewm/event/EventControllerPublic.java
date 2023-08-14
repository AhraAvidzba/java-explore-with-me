package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@Valid GetEventsCriteria getEventsCriteria) {
        List<EventShortDto> eventShortsDto = eventService.getEvents(getEventsCriteria);
        log.info("Возвращается список с краткой информацией о событии");
        return eventShortsDto;
    }

    @GetMapping("/{id}")
    public EventOutDto getFullEventById(@PathVariable(name = "id") Long eventId) {
        EventOutDto eventOutDto = eventService.getFullEventById(eventId);
        log.info("Возвращается полная информация о событии");
        return eventOutDto;
    }
}
