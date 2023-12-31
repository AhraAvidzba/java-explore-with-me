package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.event.dto.EventOutDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.PublicGetEventsCriteria;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@Valid PublicGetEventsCriteria publicGetEventsCriteria,
                                         HttpServletRequest request) {
        StatisticInDto statisticInDto = StatisticInDto.builder()
                .app("explore-with-me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        List<EventShortDto> eventShortsDto = eventService.getEvents(publicGetEventsCriteria, statisticInDto);
        log.info("Возвращается список с краткой информацией о событии. Эндпоинт {}", request.getRequestURI());
        return eventShortsDto;
    }

    @GetMapping("/{id}")
    public EventOutDto getFullEventById(@PathVariable(name = "id") Long eventId,
                                        HttpServletRequest request) {
        StatisticInDto statisticInDto = StatisticInDto.builder()
                .app("explore-with-me")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        EventOutDto eventOutDto = eventService.getFullEventById(eventId, statisticInDto);
        log.info("Возвращается полная информация о событии. Эндпоинт {}", request.getRequestURI());
        return eventOutDto;
    }
}
