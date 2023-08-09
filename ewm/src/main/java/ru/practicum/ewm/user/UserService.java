package ru.practicum.ewm.user;


import ru.practicum.ewm.user.dto.ShortUserDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto saveUser(ShortUserDto shortUserDto);

    void deleteUser(Long id);
}
