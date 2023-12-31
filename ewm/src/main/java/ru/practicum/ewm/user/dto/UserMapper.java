package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.User;

import java.util.List;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserInDto toUserInDto(User user) {
        return UserInDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static User toUser(UserInDto userInDto) {
        return User.builder()
                .email(userInDto.getEmail())
                .name(userInDto.getName())
                .build();
    }

    public static UserWithFriendshipDto toUserWithFriendsAndSubscribersDto(User user,
                                                                           List<UserShortDto> friends,
                                                                           List<UserShortDto> friendsEventVisitSubscribers,
                                                                           List<UserShortDto> friendsEventPublishSubscribers) {
        return UserWithFriendshipDto.builder()
                .build();
    }
}
