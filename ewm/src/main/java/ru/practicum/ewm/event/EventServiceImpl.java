package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.exceptions.ContentAlreadyExistException;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.exceptions.UnavailableOperationException;
import ru.practicum.ewm.location.Location;
import ru.practicum.ewm.location.LocationRepository;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.request.ParticipationRequest;
import ru.practicum.ewm.request.RequestRepository;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
//    private final StatisticClient statisticClient;


    @Override
    public List<EventShortDto> getUserEvents(int from, int size, Long userId) {
        checkUser(userId);
        PageRequest pageable = PageRequest.of(from, size, Sort.by("id").ascending());
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutDto addEvent(EventInDto eventInDto, Long userId) {
        checkEventTime(eventInDto.getEventDate(), LocalDateTime.now(), 2);
        Category category = categoryRepository.findById(eventInDto.getCategory())
                .orElseThrow(() -> new ContentNotFoundException("Катагория не найдена"));
        User user = checkUser(userId);
        Location location = checkLocation(eventInDto.getLocation());
        State state = State.PENDING;
        LocalDateTime publishedOn = null;
        if (!eventInDto.isRequestModeration()) {
            state = State.PUBLISHED;
            publishedOn = LocalDateTime.now();
        }
        Event event = Event.builder()
                .annotation(eventInDto.getAnnotation())
                .category(category)
                .description(eventInDto.getDescription())
                .eventDate(eventInDto.getEventDate())
                .location(location)
                .paid(eventInDto.getPaid())
                .participantLimit(eventInDto.getParticipantLimit())
                .requestModeration(eventInDto.isRequestModeration())
                .title(eventInDto.getTitle())
                //дополнительные данные
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .initiator(user)
                .publishedOn(publishedOn)
                .state(state)
                .views(0)
                .build();
        Event savedEvent = eventRepository.save(event);
        return EventMapper.mapToEventOutDto(savedEvent);
    }

    @Override
    public EventOutDto getFullUserEvent(Long eventId, Long userId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new UnavailableOperationException("Просматривать полную информацию о событии может только его инициатор");
        }
        return EventMapper.mapToEventOutDto(event);
    }

    @Override
    public EventOutDto editEvent(UpdateEventRequestDto eventDto, Long userId, Long eventId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new UnavailableOperationException("Просматривать полную информацию о событии может только его инициатор");
        }
        checkEventTime(eventDto.getEventDate(), LocalDateTime.now(), 2);
        if (State.PUBLISHED.equals(event.getState())) {
            throw new UnavailableOperationException("Статус события должен быть отмененным или ожидающим модерацию");
        }
        State state = null;
        if (eventDto.getStateAction() != null) {
            StateAction stateAction = eventDto.getStateAction();
            switch (stateAction) {
                case SEND_TO_REVIEW:
                    state = State.PENDING;
                    break;
                case CANCEL_REVIEW:
                    state = State.CANCELED;
                    break;
                default:
                    throw new UnavailableOperationException("Недопустимый формат state");
            }
        }
        updateNonNullFields(event, eventDto, state);
        Event updatedEvent = eventRepository.save(event);
        return EventMapper.mapToEventOutDto(updatedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsForUserEvent(Long eventId, Long userId) {
        checkUser(userId);
        Event event = checkEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new UnavailableOperationException("Просматривать информацию о запросах на участие в событии может только его инициатор");
        }
        List<ParticipationRequest> requests = requestRepository.findRequestByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeStatusForUserEventsRequests(EventRequestStatusUpdateRequest requestsAndStatus, Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ContentNotFoundException("Событие не найдено"));
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == event.getConfirmedRequests()) {
            throw new ContentAlreadyExistException("Лимит на количество участников в этом событии достигнут");
        }
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ContentAlreadyExistException("Изменять статус запросо на участие в событии может только инициатор события");
        }
        List<ParticipationRequest> requests = requestRepository.findRequestByIdInAndEventId(requestsAndStatus.getRequestIds(), eventId);
        if (requests.size() < requestsAndStatus.getRequestIds().size()) {
            throw new UnavailableOperationException("Не все указанные id в запросах соответствуют запрашиваемому Событию");
        }
        for (ParticipationRequest request : requests) {
            if (request.getStatus().equals(Status.PENDING)) {
                if (event.getParticipantLimit() == event.getConfirmedRequests()) {
                    request.setStatus(requestsAndStatus.getStatus());
                    request.getEvent().setConfirmedRequests(request.getEvent().getConfirmedRequests() + 1);
                } else {
                    request.setStatus(Status.REJECTED);
                }
            }
        }
        eventRepository.save(event);
        List<ParticipationRequest> confirmedRequests = requests.stream()
                .filter(x -> x.getStatus().equals(Status.CONFIRMED))
                .collect(Collectors.toList());
        List<ParticipationRequest> rejectedRequests = requests.stream()
                .filter(x -> x.getStatus().equals(Status.REJECTED))
                .collect(Collectors.toList());
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests.stream()
                        .map(RequestMapper::mapToRequestDto).collect(Collectors.toList()))
                .rejectedRequests(rejectedRequests.stream()
                        .map(RequestMapper::mapToRequestDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<EventShortDto> getEvents(PublicGetEventsCriteria publicGetEventsCriteria) {
        List<Event> events = eventRepository.findEventsByPublicCriteria(publicGetEventsCriteria);
        events.forEach(event -> event.setViews(event.getViews() + 1)); //увеличение количества просмотров на 1 (только у публичного эндпоинта)
        eventRepository.saveAll(events);
        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutDto getFullEventById(Long eventId) {
        Event event = eventRepository.findEventByIdAndState(eventId, State.PUBLISHED)
                .orElseThrow(() -> new ContentNotFoundException("Опубликованного события с id " + eventId + " не найдено."));
        event.setViews(event.getViews() + 1); //увеличение количества просмотров на 1 (только у публичного эндпоинта)
        eventRepository.save(event);
        return EventMapper.mapToEventOutDto(event);
    }

    @Override
    public List<EventOutDto> getFullEvents(AdminGetEventsCriteria adminGetEventsCriteria) {
        List<Event> events = eventRepository.findEventsByAdminCriteria(adminGetEventsCriteria);
        return events.stream()
                .map(EventMapper::mapToEventOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventOutDto editEventByAdmin(UpdateEventRequestDto eventDto, Long eventId) {
        Event event = checkEvent(eventId);
        checkEventTime(eventDto.getEventDate(), LocalDateTime.now(), 1);
        if (StateAction.PUBLISH_EVENT.equals(eventDto.getStateAction()) && !State.PENDING.equals(event.getState())) {
            throw new UnavailableOperationException("Событие можно публиковать, только если оно в состоянии ожидания публикации.");
        }
        if (StateAction.REJECT_EVENT.equals(eventDto.getStateAction()) && State.PUBLISHED.equals(event.getState())) {
            throw new UnavailableOperationException("Событие можно отклонить, только если оно еще не опубликовано.");
        }
        State state = null;
        if (eventDto.getStateAction() != null) {
            StateAction stateAction = eventDto.getStateAction();
            switch (stateAction) {
                case PUBLISH_EVENT:
                    state = State.PUBLISHED;
                    break;
                case REJECT_EVENT:
                    state = State.CANCELED;
                    break;
                default:
                    throw new UnavailableOperationException("Недопустимый формат state");
            }
        }
        updateNonNullFields(event, eventDto, state);
        Event updatedEvent = eventRepository.save(event);
        return EventMapper.mapToEventOutDto(updatedEvent);
    }

    private void updateNonNullFields(Event event, UpdateEventRequestDto eventDto, State state) {
        Category category = checkCategory(eventDto.getCategory());
        Location location = checkLocation(eventDto.getLocation());
        event.setAnnotation(eventDto.getAnnotation() != null ? eventDto.getAnnotation() : event.getAnnotation());
        event.setCategory(eventDto.getCategory() != null ? category : event.getCategory());
        event.setDescription(eventDto.getDescription() != null ? eventDto.getDescription() : event.getDescription());
        event.setEventDate(eventDto.getEventDate() != null ? eventDto.getEventDate() : event.getEventDate());
        event.setLocation(eventDto.getLocation() != null ? location : event.getLocation());
        event.setPaid(eventDto.getPaid() != null ? eventDto.getPaid() : event.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() : event.isRequestModeration());
        event.setTitle(eventDto.getTitle() != null ? eventDto.getTitle() : event.getTitle());
        event.setState(state != null ? state : event.getState());
        event.setPublishedOn(State.PUBLISHED.equals(state) ? LocalDateTime.now() : null);
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователя с id = " + userId + " не существует"));
    }

    private Event checkEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ContentNotFoundException("События с id = " + eventId + " не существует"));
    }

    private Category checkCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ContentNotFoundException("Категории с id = " + categoryId + " не существует"));
    }

    private Location checkLocation(LocationDto locationDto) {
        //точность определения существования координат в бд
        float accuracy = 1e-11f;
        Optional<Location> location = locationRepository.findLocationByLatAndLon(locationDto.getLat(), locationDto.getLon(), accuracy);
        return location.orElseGet(() -> locationRepository.save(LocationMapper.mapToLocation(locationDto)));
    }

    private void checkEventTime(LocalDateTime eventTime, LocalDateTime comparedTime, int hoursReserve) {
        if (eventTime.isBefore(comparedTime.plusHours(hoursReserve))) {
            throw new UnavailableOperationException("Время начала публикуемого события не может быть раньше, чем через " + hoursReserve + " час/a от текущего момента");
        }
    }

}
