package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;
import ru.practicum.ewm.user.dto.UserWithFriendsDto;
import ru.practicum.ewm.validations.Create;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
@Validated
public class UserControllerPrivate {
    private final UserService userService;

    @PostMapping("/friend/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsDto sendFriendshipRequest(@PathVariable Long userId,
                                                    @PathVariable Long friendId,
                                                    HttpServletRequest request) {
        UserWithFriendsDto savedUserDto = userService.sendFriendshipRequest(userId, friendId);
        log.info("Запрос дружбы отправлен. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/eventVisitSubscribe/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsDto subscribeToFriendsEventVisits(@PathVariable Long userId,
                                               @PathVariable Long friendId,
                                         HttpServletRequest request) {
        UserWithFriendsDto savedUserDto = userService.subscribeToFriendsEventVisits(userId, friendId);
        log.info("Оформлена подписка на посещаемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }

    @PostMapping("/eventPublishSubscribe/{friendId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserWithFriendsDto subscribeToFriendsEventPublishes(@PathVariable Long userId,
                                               @PathVariable Long friendId,
                                               HttpServletRequest request) {
        UserWithFriendsDto savedUserDto = userService.subscribeToFriendsEventPublishes(userId, friendId);
        log.info("Оформлена подписка на публикуемые другом события. Эндпоинт {}", request.getRequestURI());
        return savedUserDto;
    }
}
