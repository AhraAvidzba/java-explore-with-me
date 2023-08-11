package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Возвращаются все запросы на участия от пользователя с id {}", userId);
        return requestService.getRequests(userId);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@RequestParam Long eventId,
                                              @PathVariable Long userId) {
        ParticipationRequestDto participationRequestDto = requestService.addRequest(eventId, userId);
        log.info("Отправляется запрос на участие в событии");
        return participationRequestDto;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(code = HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Обновляется апрос на участие в событии");
        return requestService.cancelRequest(userId, requestId);
    }
}
