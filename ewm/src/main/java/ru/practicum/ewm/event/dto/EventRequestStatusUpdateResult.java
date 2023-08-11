package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
