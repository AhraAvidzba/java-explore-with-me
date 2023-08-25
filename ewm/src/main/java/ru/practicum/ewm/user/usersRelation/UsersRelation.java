package ru.practicum.ewm.user.usersRelation;

import lombok.*;
import ru.practicum.ewm.user.User;

import javax.persistence.*;

@Builder
@Entity
@Table(name = "users_relation")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class UsersRelation {
    @EmbeddedId
    private UsersRelationId usersRelationId;
    @Column
    private Boolean eventVisitSubscriber;
    @Column
    private Boolean eventPublishSubscriber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @MapsId("friendId")
    private User friend;
}
