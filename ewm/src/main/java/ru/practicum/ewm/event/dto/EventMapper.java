package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.location.dto.LocationMapper;
import ru.practicum.ewm.user.dto.UserMapper;

public class EventMapper {
    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .views(event.getViews())
                .category(CategoryMapper.mapToCategoryOutDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests().size())
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .build();
    }

    public static EventOutDto mapToEventOutDto(Event event) {
        return EventOutDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .views(event.getViews())
                .category(CategoryMapper.mapToCategoryOutDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests().size())
                .paid(event.getPaid())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .build();
    }
}
