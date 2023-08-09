package ru.practicum.ewm.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.Category;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findEventByCategoryId(Long catId);
}
