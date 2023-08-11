package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.exceptions.ContentNotFoundException;
import ru.practicum.ewm.exceptions.UnavailableOperationException;
import ru.practicum.ewm.location.Location;
import ru.practicum.ewm.location.LocationRepository;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;


    @Override
    public List<EventShortDto> getUserEvents(int from, int size, Long userId) {
        return null;
    }

    @Override
    public EventOutDto addEvent(EventInDto eventInDto, Long userId) {
        if (eventInDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new UnavailableOperationException("Время начала публикуемого события не может быть раньше, чем через два часа от текущего момента");
        }
        Category category = categoryRepository.findById(eventInDto.getCategory())
                .orElseThrow(() -> new ContentNotFoundException("Катагория не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ContentNotFoundException("Пользователя с id = " + userId + " не существует"));
        Location location = locationRepository.findLocationByLatAndLon(eventInDto.getLocation().getLat(), eventInDto.getLocation().getLon())
                .orElse(locationRepository.save(LocationMapper.mapToLocation(eventInDto.getLocation())));
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
                .publishedOn(null)
                .state(State.PENDING)
                .views(0)
                .build();
        Event savedEvent = eventRepository.save(event);
        return EventMapper.mapToEventOutDto(savedEvent);
    }

    @Override
    public EventOutDto getFullUserEvent(Long eventId, Long userId) {
        return null;
    }

    @Override
    public EventOutDto editEvent(UpdateEventRequestDto event, Long userId, Long eventId) {
        return null;
    }

    @Override
    public ParticipationRequestDto getRequestsForUserEvent(Long eventId, Long userId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult changeStatusForUserEventsRequests(EventRequestStatusUpdateRequest requestsAndStatus, Long eventId, Long userId) {
        return null;
    }
}
