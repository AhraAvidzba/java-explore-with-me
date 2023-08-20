package ru.practicum.ewm.user.usersRelation;

import lombok.*;
import ru.practicum.ewm.user.User;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersRelationId implements Serializable {
    public Long user;
    public Long friend;

}
