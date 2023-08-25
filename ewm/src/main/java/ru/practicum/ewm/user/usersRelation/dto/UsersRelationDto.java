package ru.practicum.ewm.user.usersRelation.dto;

import lombok.*;
import ru.practicum.ewm.user.FriendStatus;
import ru.practicum.ewm.user.dto.UserShortDto;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UsersRelationDto {
    private UserShortDto friend;
    private Boolean eventVisitSubscriber;
    private Boolean eventPublishSubscriber;
    private FriendStatus friendStatus;
}
