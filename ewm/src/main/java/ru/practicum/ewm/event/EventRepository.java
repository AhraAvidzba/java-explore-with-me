package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findByInitiatorId(Long userId, Pageable pageable);
    List<Event> findByInitiatorId(Long userId);
    List<Event> findByInitiatorIdAndStateAndEventDateIsAfter(Long userId, State state, LocalDateTime date);

    List<Event> findEventByCategoryId(Long catId);

    List<Event> findEventByIdIn(List<Long> catIds);

    Optional<Event> findEventByIdAndState(Long eventId, State state);
}
