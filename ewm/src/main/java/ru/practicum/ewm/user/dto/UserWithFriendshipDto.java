package ru.practicum.ewm.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.user.usersRelation.dto.UsersRelationDto;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class UserWithFriendshipDto {
    private Long id;
    private String name;
    private String email;
    List<UsersRelationDto> friendship;
}
