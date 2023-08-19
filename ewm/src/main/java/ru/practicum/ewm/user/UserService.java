package ru.practicum.ewm.user;


import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto saveUser(UserInDto userInDto);

    void deleteUser(Long id);
}
