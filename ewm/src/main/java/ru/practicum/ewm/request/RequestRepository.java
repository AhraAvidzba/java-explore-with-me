package ru.practicum.ewm.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findRequestByRequesterId(Long userId);

    List<ParticipationRequest> findRequestByIdInAndEventId(List<Long> requestIds, Long eventId);

    List<ParticipationRequest> findRequestByEventId(Long eventId);

    Optional<ParticipationRequest> findRequestByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findByRequesterIdAndEventStateAndEventEventDateIsAfterAndShowToEventSubscribers(Long requesterId, State state, LocalDateTime date, Boolean isShowToSubscribers);
}
