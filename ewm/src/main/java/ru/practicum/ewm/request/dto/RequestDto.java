package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.request.Status;
import ru.practicum.ewm.user.User;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
