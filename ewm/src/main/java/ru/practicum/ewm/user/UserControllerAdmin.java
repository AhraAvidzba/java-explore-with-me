package ru.practicum.ewm.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.dto.ShortUserDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Возвращается список запрашиваемых пользователей");
        return userService.getUsers(ids, from, size);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody ShortUserDto shortUserDto) {
        UserDto savedUserDto = userService.saveUser(shortUserDto);
        log.info("Пользователь сохраняется, id = {}", savedUserDto.getId());
        return savedUserDto;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь удален, id = {}", userId);
    }
}
