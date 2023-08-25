package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserWithFriendshipDto;

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
    public UserWithFriendshipDto sendFriendshipRequest(@PathVariable Long userId,
                                                       @PathVariable Long friendId,
                                                       HttpServletRequest request) {
        UserWithFriendshipDto savedUserDto = userService.sendFriendshipRequest(userId, friendId);
        log.info("Запрос дружбы отправлен. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/subscribe/event/visits/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendshipDto subscribeToFriendsEventVisits(@PathVariable Long userId,
                                                               @PathVariable Long friendId,
                                                               HttpServletRequest request) {
        UserWithFriendshipDto savedUserDto = userService.subscribeToFriendsEventVisits(userId, friendId);
        log.info("Оформлена подписка на посещаемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/subscribe/event/published/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendshipDto subscribeToFriendsEventPublishes(@PathVariable Long userId,
                                                                  @PathVariable Long friendId,
                                                                  HttpServletRequest request) {
        UserWithFriendshipDto savedUserDto = userService.subscribeToFriendsEventPublishes(userId, friendId);
        log.info("Оформлена подписка на публикуемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }
}
