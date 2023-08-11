package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.EventInDto;
import ru.practicum.ewm.event.dto.EventOutDto;

public interface EventService {
    //Методы для админов
    EventOutDto addEvent(EventInDto eventInDto, Long userId);
}
