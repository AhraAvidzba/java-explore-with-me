package ru.practicum.ewm.user.usersRelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsesRelationRepository extends JpaRepository<UsersRelation, UsersRelationId> {
}
