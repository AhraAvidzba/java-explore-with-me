package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.request.Status;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;
}
