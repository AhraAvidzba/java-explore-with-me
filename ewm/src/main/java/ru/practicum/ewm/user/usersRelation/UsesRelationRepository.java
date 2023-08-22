package ru.practicum.ewm.user.usersRelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsesRelationRepository extends JpaRepository<UsersRelation, UsersRelationId> {
    //    List<UsersRelation> findByUserIdInAndFriendIdIn(List<Long> userId, List<Long> friendId);
    List<UsersRelation> findByUsersRelationIdIn(List<UsersRelationId> usersRelationIds);

    List<UsersRelation> findByUsersRelationIdUserIdOrFriendId(Long userId, Long friendId);

}
