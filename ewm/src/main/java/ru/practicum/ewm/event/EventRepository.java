package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventByCategoryId(Long catId);

    List<Event> findEventByIdIn(List<Long> catIds);
}
