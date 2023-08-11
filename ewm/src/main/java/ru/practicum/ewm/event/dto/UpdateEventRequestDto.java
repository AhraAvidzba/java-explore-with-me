package ru.practicum.ewm.event.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.category.dto.CategoryOutDto;
import ru.practicum.ewm.event.State;
import ru.practicum.ewm.event.StateAction;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UpdateEventRequestDto {
    private String annotation;
    private CategoryOutDto category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private StateAction stateAction;
    private String title;

}
