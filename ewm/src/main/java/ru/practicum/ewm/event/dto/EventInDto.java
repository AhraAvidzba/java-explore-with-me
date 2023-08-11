package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EventInDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    @Length(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
