package ru.practicum.ewm.user.usersRelation;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.user.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
public class UsersRelationId implements Serializable {
    public User user;
    public User friend;

}
