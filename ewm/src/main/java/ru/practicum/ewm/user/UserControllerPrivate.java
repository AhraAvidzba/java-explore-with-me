package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserWithFriendsAndSubscribersDto;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Validated
public class UserControllerPrivate {
    private final UserService userService;

    @PostMapping("/friend/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsAndSubscribersDto sendFriendshipRequest(@PathVariable Long userId,
                                                                  @PathVariable Long friendId,
                                                                  HttpServletRequest request) {
        UserWithFriendsAndSubscribersDto savedUserDto = userService.sendFriendshipRequest(userId, friendId);
        log.info("Запрос дружбы отправлен. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/eventVisitSubscribe/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsAndSubscribersDto subscribeToFriendsEventVisits(@PathVariable Long userId,
                                                                          @PathVariable Long friendId,
                                                                          HttpServletRequest request) {
        UserWithFriendsAndSubscribersDto savedUserDto = userService.subscribeToFriendsEventVisits(userId, friendId);
        log.info("Оформлена подписка на посещаемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/eventPublishSubscribe/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsAndSubscribersDto subscribeToFriendsEventPublishes(@PathVariable Long userId,
                                                                             @PathVariable Long friendId,
                                                                             HttpServletRequest request) {
        UserWithFriendsAndSubscribersDto savedUserDto = userService.subscribeToFriendsEventPublishes(userId, friendId);
        log.info("Оформлена подписка на публикуемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }
}
