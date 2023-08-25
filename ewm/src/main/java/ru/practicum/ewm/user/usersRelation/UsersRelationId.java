package ru.practicum.ewm.user.usersRelation;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UsersRelationId implements Serializable {
    @Column
    public Long userId;
    @Column
    public Long friendId;

}
