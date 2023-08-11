package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.exceptions.EditingNotAllowedException;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<RequestDto> getRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователь не найден"));
        List<Request> requests = requestRepository.findRequestByRequesterId(userId);
        return requests.stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(Long eventId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ContentNotFoundException("Событие не найдено"));
        requestRepository.findRequestByRequesterIdAndEventId(userId, eventId)
                .ifPresent((x) ->
                {
                    throw new ContentAlreadyExistException("Запрос на участие в данном событии был ранее уже отправлен пользователем");
                });
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ContentAlreadyExistException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ContentAlreadyExistException("Событие должно быть опубликованным");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ContentAlreadyExistException("Лимит на количество участников в этом событии достигнут");
        }
        Request request = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
        if (!event.isRequestModeration()) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        Request savedRequest = requestRepository.save(request);
        return RequestMapper.mapToRequestDto(savedRequest);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователь не найден"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ContentAlreadyExistException("Запрос не найден"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new EditingNotAllowedException("Отменить запрос может только его владелец");
        }
        if (request.getStatus().equals(Status.CONFIRMED)) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        } else {
            throw new ContentNotFoundException("Подтвержденных запросов на участие не найдено");
        }
        request.setStatus(Status.PENDING);
        return RequestMapper.mapToRequestDto(requestRepository.save(request));
    }
}
