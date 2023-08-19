package ru.practicum.ewm.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .subscribers(user.getSubscribers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
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
                .subscribers(new ArrayList<>())
                .build();
    }
}
