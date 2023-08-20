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
@IdClass(UsersRelationId.class)
public class UsersRelation {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;
    @Column
    private Boolean eventVisitSubscriber;
    @Column
    private Boolean eventPublishSubscriber;
}
