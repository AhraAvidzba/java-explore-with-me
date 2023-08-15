package ru.practicum.ewm.event;

import ru.practicum.ewm.event.dto.AdminGetEventsCriteria;
import ru.practicum.ewm.event.dto.PublicGetEventsCriteria;

import java.util.List;

public interface EventRepositoryCustom {
    List<Event> findEventsByPublicCriteria(PublicGetEventsCriteria publicGetEventsCriteria);

    List<Event> findEventsByAdminCriteria(AdminGetEventsCriteria adminGetEventsCriteria);
}
