package ru.practicum.ewm.user.usersRelation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.user.FriendStatus;
import ru.practicum.ewm.user.User;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.usersRelation.UsersRelation;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UsersRelationMapper {
    public static List<UsersRelationDto> toUsersRelationDtoList(List<UsersRelation> usersRelations, FriendStatus friendStatus, Long userId) {
        List<UsersRelationDto> usersRelationsDto = new ArrayList<>();
        for (UsersRelation usersRelation : usersRelations) {
            User friend = userId.equals(usersRelation.getFriend().getId()) ? usersRelation.getUser() : usersRelation.getFriend();
            UserShortDto userShortDto = UserMapper.toUserShortDto(friend);
            UsersRelationDto usersRelationDto = UsersRelationDto.builder()
                    .friend(userShortDto)
                    .eventVisitSubscriber(usersRelation.getEventVisitSubscriber())
                    .eventPublishSubscriber(usersRelation.getEventPublishSubscriber())
                    .friendStatus(friendStatus)
                    .build();
            usersRelationsDto.add(usersRelationDto);
        }
        return usersRelationsDto;
    }
}
