package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.user.User;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserWithFriendsDto {
    private Long id;
    private String name;
    private String email;
    private List<User> friends;
    private List<User> friendsEventVisitSubscribers;
    private List<User> friendsEventPublishSubscribers;
}
