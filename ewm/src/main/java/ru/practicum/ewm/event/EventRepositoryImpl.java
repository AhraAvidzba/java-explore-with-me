package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.dto.GetEventsCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Event> findTest(GetEventsCriteria getEventsCriteria) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime rangeStart = LocalDateTime.parse(getEventsCriteria.getRangeStart(), formatter);
        LocalDateTime rangeEnd = LocalDateTime.parse(getEventsCriteria.getRangeEnd(), formatter);

        List<Predicate> predicatesList = new ArrayList<>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> root = cq.from(Event.class);
        Predicate titlePredicate = cb.like(cb.lower(root.get("title")), "%" + getEventsCriteria.getText().toLowerCase() + "%");
        Predicate annotationPredicate = cb.like(cb.lower(root.get("annotation")), "%" + getEventsCriteria.getText().toLowerCase() + "%");

        predicatesList.add(cb.or(titlePredicate, annotationPredicate));
        predicatesList.add(root.get("category").in(getEventsCriteria.getCategories()));
        predicatesList.add(cb.between(root.get("eventDate"), rangeStart, rangeEnd));

        if (getEventsCriteria.getOnlyAvailable()) {
            predicatesList.add(cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        if (getEventsCriteria.getPaid() != null) {
            predicatesList.add(cb.equal(root.get("paid"), getEventsCriteria.getPaid()));
        }

        Predicate[] predicatesArray = predicatesList.toArray(new Predicate[0]);
        cq.where(predicatesArray);

        String strSort = null;
        switch (getEventsCriteria.getSort()) {
            case EVENT_DATE:
                strSort = "eventDate";
                break;
            case VIEWS:
                strSort = "views";
        }

        cq.orderBy(cb.asc(root.get(strSort)));
        TypedQuery<Event> query = em.createQuery(cq);

        return query
                .setMaxResults(getEventsCriteria.getSize())
                .setFirstResult(getEventsCriteria.getFrom())
                .getResultList();
    }
}
