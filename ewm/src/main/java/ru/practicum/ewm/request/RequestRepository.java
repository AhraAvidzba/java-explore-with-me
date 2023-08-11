package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.category.Category;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findRequestByRequesterId(Long userId);
    Optional<Request> findRequestByRequesterIdAndEventId(Long userId, Long eventId);
}
