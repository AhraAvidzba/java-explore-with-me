package ru.practicum.ewm.request.dto;

import lombok.*;
import ru.practicum.ewm.request.Status;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
    private Boolean showToEventSubscribers;
}
