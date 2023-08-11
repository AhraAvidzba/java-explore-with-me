package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventInDto;
import ru.practicum.ewm.event.dto.EventOutDto;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/event")
@RequiredArgsConstructor
@Validated
public class EventControllerPrivate {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventOutDto addEvent(@RequestBody EventInDto eventInDto,
                                @PathVariable Long userId) {
        EventOutDto eventOutDto = eventService.addEvent(eventInDto, userId);
        log.info("Событие добавлено");
        return eventOutDto;
    }
}
