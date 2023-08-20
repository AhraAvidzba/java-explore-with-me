package ru.practicum.ewm.user.usersRelation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.user.User;

import java.util.List;
import java.util.Optional;

public interface UsesRelationRepository extends JpaRepository<UsesRelationRepository, Long> {
}
