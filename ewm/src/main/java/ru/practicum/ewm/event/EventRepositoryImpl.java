package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.dto.AdminGetEventsCriteria;
import ru.practicum.ewm.event.dto.PublicGetEventsCriteria;

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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Event> findEventsByPublicCriteria(PublicGetEventsCriteria publicGetEventsCriteria) {
        List<Predicate> predicatesList = new ArrayList<>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> root = cq.from(Event.class);
        if (publicGetEventsCriteria.getText() != null) {
            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), "%" + publicGetEventsCriteria.getText().toLowerCase() + "%");
            Predicate annotationPredicate = cb.like(cb.lower(root.get("annotation")), "%" + publicGetEventsCriteria.getText().toLowerCase() + "%");
            predicatesList.add(cb.or(titlePredicate, annotationPredicate));
        }
        if (publicGetEventsCriteria.getCategories() != null) {
            predicatesList.add(root.get("category").in(publicGetEventsCriteria.getCategories()));
        }
        predicatesList.add(cb.equal(root.get("state"), State.PUBLISHED));

        setDatePredicates(cb, root, publicGetEventsCriteria.getRangeStart(), publicGetEventsCriteria.getRangeEnd(), predicatesList);

        if (publicGetEventsCriteria.getOnlyAvailable()) {
            predicatesList.add(cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit")));
        }
        if (publicGetEventsCriteria.getPaid() != null) {
            predicatesList.add(cb.equal(root.get("paid"), publicGetEventsCriteria.getPaid()));
        }

        Predicate[] predicatesArray = predicatesList.toArray(new Predicate[0]);
        cq.where(predicatesArray);

        String strSort = null;
        if (publicGetEventsCriteria.getSort() != null) {
            switch (publicGetEventsCriteria.getSort()) {
                case EVENT_DATE:
                    strSort = "eventDate";
                    break;
                case VIEWS:
                    strSort = "views";
            }
            cq.orderBy(cb.asc(root.get(strSort)));
        } else {
            cq.orderBy(cb.asc(root.get("id")));
        }

        TypedQuery<Event> query = em.createQuery(cq);

        return query
                .setMaxResults(publicGetEventsCriteria.getSize())
                .setFirstResult(publicGetEventsCriteria.getFrom())
                .getResultList();
    }

    @Override
    public List<Event> findEventsByAdminCriteria(AdminGetEventsCriteria adminGetEventsCriteria) {
        List<Predicate> predicatesList = new ArrayList<>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);

        Root<Event> root = cq.from(Event.class);

        if (adminGetEventsCriteria.getUsers() != null) {
            predicatesList.add(root.get("initiator").in(adminGetEventsCriteria.getUsers()));
        }
        if (adminGetEventsCriteria.getStates() != null) {
            predicatesList.add(root.get("state").in(adminGetEventsCriteria.getStates()));
        }
        if (adminGetEventsCriteria.getCategories() != null) {
            predicatesList.add(root.get("category").in(adminGetEventsCriteria.getCategories()));
        }

        setDatePredicates(cb, root, adminGetEventsCriteria.getRangeStart(), adminGetEventsCriteria.getRangeEnd(), predicatesList);

        Predicate[] predicatesArray = predicatesList.toArray(new Predicate[0]);
        cq.where(predicatesArray);

        cq.orderBy(cb.asc(root.get("id")));
        TypedQuery<Event> query = em.createQuery(cq);

        return query
                .setMaxResults(adminGetEventsCriteria.getSize())
                .setFirstResult(adminGetEventsCriteria.getFrom())
                .getResultList();
    }

    private void setDatePredicates(CriteriaBuilder cb, Root<Event> root, String strRangeStart, String strRangeEnd, List<Predicate> predicatesList) {
        if (strRangeStart != null && strRangeEnd != null) {
            LocalDateTime rangeStart = LocalDateTime.parse(strRangeStart, formatter);
            LocalDateTime rangeEnd = LocalDateTime.parse(strRangeEnd, formatter);
            predicatesList.add(cb.between(root.get("eventDate"), rangeStart, rangeEnd));
        } else {
            LocalDateTime rangeStart = LocalDateTime.now();
            predicatesList.add(cb.greaterThan(root.get("eventDate"), rangeStart));
        }
    }
}
