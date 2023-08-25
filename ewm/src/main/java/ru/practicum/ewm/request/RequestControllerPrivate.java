package ru.practicum.ewm.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
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
                                              @PathVariable Long userId,
                                              @RequestParam(defaultValue = "true") Boolean showToSubscribers,
                                              HttpServletRequest request) {
        ParticipationRequestDto participationRequestDto = requestService.addRequest(eventId, userId, showToSubscribers);
        log.info("Отправляется запрос на участие в событии. Эндпоинт {}", request.getRequestURI());
        return participationRequestDto;
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(code = HttpStatus.OK)
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId,
                                                 HttpServletRequest request) {
        log.info("Обновляется запрос на участие в событии. Эндпоинт {}", request.getRequestURI());
        return requestService.cancelRequest(userId, requestId);
    }
}
