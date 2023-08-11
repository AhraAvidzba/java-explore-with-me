package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findRequestByRequesterId(Long userId);

    Optional<ParticipationRequest> findRequestByRequesterIdAndEventId(Long userId, Long eventId);
}
