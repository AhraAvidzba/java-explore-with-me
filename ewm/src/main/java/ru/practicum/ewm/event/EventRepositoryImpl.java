package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.dto.GetEventsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements  EventRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Event> findTest(GetEventsCriteria getEventsCriteria) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> event = cq.from(Event.class);
        Predicate titlePredicate = cb.like(event.get("title"), "%" + getEventsCriteria.getText() + "%");
        cq.where(titlePredicate);

        TypedQuery<Event> query = em.createQuery(cq);
        return query.getResultList();
    }
}
