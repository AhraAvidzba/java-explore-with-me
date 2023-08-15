package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.PublicGetEventsCriteria;

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
    public List<EventShortDto> getEvents(@Valid PublicGetEventsCriteria publicGetEventsCriteria) {
        List<EventShortDto> eventShortsDto = eventService.getEvents(publicGetEventsCriteria);
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
