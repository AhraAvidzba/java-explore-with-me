package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;
import ru.practicum.ewm.validations.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class UserControllerAdmin {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(defaultValue = "-1") List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Возвращается список запрашиваемых пользователей");
        return userService.getUsers(ids, from, size);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto saveUser(@Validated(Create.class) @RequestBody UserInDto userInDto) {
        UserDto savedUserDto = userService.saveUser(userInDto);
        log.info("Пользователь сохранен, id = {}", savedUserDto.getId());
        return savedUserDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь удален, id = {}", userId);
    }
}
