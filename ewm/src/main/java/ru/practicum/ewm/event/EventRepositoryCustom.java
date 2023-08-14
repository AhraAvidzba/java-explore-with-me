package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.GetEventsCriteria;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findEventsByCriteria(GetEventsCriteria getEventsCriteria);
}
