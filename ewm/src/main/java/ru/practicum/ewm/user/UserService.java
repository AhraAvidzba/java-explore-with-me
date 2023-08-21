package ru.practicum.ewm.user;


import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserInDto;
import ru.practicum.ewm.user.dto.UserWithFriendshipDto;

import java.util.List;

public interface UserService {
    //Методы для админов
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto saveUser(UserInDto userInDto);

    void deleteUser(Long id);

    //Приватные методы
    UserWithFriendshipDto sendFriendshipRequest(Long userId, Long friendId);

    UserWithFriendshipDto subscribeToFriendsEventVisits(Long userId, Long friendId);

    UserWithFriendshipDto subscribeToFriendsEventPublishes(Long userId, Long friendId);

}
