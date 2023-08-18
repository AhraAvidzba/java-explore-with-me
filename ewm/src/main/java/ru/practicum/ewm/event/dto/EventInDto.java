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
    @Length(min = 20, max = 7000)
    @NotNull
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
