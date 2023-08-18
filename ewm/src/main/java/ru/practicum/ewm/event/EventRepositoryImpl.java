package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.dto.AdminGetEventsCriteria;
import ru.practicum.ewm.event.dto.PublicGetEventsCriteria;
import ru.practicum.ewm.request.ParticipationRequest;
import ru.practicum.ewm.request.ParticipationRequest_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.criterion.Projections.count;

@Repository
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepositoryCustom {

    private final EntityManager em;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Event> findEventsByPublicCriteria(PublicGetEventsCriteria publicGetEventsCriteria) {
        List<Predicate> predicatesList = new ArrayList<>();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> cqEvent = cb.createQuery(Event.class);
        Root<Event> rootEvent = cqEvent.from(Event.class);




//        cqEvent.select(rootEvent)
//                .where(cb.equal(requestJoin.get("event"), 1));
//        List<Event> requests = em.createQuery(cqEvent).getResultList();

//        if (publicGetEventsCriteria.getOnlyAvailable()) {
//            predicatesList.add(
//                    cb.equal(requestJoin.get("event"), rootEvent.get("id"))
////                    cb.greaterThan(rootEvent.get("participantLimit"),
////                    cb.count(cb.equal(requestJoin.get("event"), rootEvent.get("id"))))
//            );
//        }


//        CriteriaQuery<ParticipationRequest> cqRequest = cb.createQuery(ParticipationRequest.class);
//        Root<ParticipationRequest> requestRoot = cqRequest.from(ParticipationRequest.class);
//        Join<ParticipationRequest, Event> eventJoin = requestRoot.join("event");
//
//        cqRequest.select(requestRoot)
//                .where(cb.equal(eventJoin.get("id"), 1));
//        List<ParticipationRequest> requests = em.createQuery(cqRequest).getResultList();

//        System.out.println("-" .repeat(500));
//        if (!requests.isEmpty()) {
//            System.out.println(requests.get(0).getId());
//        }
//        System.out.println("-" .repeat(500));

//        if (publicGetEventsCriteria.getOnlyAvailable()) {
//            predicatesList.add(
//                    cb.greaterThan(rootEvent.get("participantLimit"),
//                    cb.count(cb.equal(eventJoin.get("id"), rootEvent.get("id"))))
//            );
//        }



        if (publicGetEventsCriteria.getText() != null) {
            Predicate titlePredicate = cb.like(cb.lower(rootEvent.get("title")), "%" + publicGetEventsCriteria.getText().toLowerCase() + "%");
            Predicate annotationPredicate = cb.like(cb.lower(rootEvent.get("annotation")), "%" + publicGetEventsCriteria.getText().toLowerCase() + "%");
            predicatesList.add(cb.or(titlePredicate, annotationPredicate));
        }
        if (publicGetEventsCriteria.getCategories() != null) {
            predicatesList.add(rootEvent.get("category").in(publicGetEventsCriteria.getCategories()));
        }
        predicatesList.add(cb.equal(rootEvent.get("state"), State.PUBLISHED));

        setDatePredicates(cb, rootEvent, publicGetEventsCriteria.getRangeStart(), publicGetEventsCriteria.getRangeEnd(), predicatesList);

//        if (publicGetEventsCriteria.getOnlyAvailable()) {
//            predicatesList.add(cb.lessThan(rootEvent.get("confirmedRequests"), rootEvent.get("participantLimit")));
//        }
        if (publicGetEventsCriteria.getPaid() != null) {
            predicatesList.add(cb.equal(rootEvent.get("paid"), publicGetEventsCriteria.getPaid()));
        }

        if (publicGetEventsCriteria.getOnlyAvailable()) {
            Join<Event, ParticipationRequest> requestJoin = rootEvent.join("confirmedRequests", JoinType.LEFT);
            Expression<Long> count = cb.count(requestJoin.get("id"));
            cqEvent.groupBy(rootEvent.get("id"));
            cqEvent.having(cb.or(cb.lessThan(count, rootEvent.get("participantLimit")), cb.equal(rootEvent.get("participantLimit"), 0)));
        }

        cqEvent.where(predicatesList.toArray(Predicate[]::new));


        String strSort = null;
        if (publicGetEventsCriteria.getSort() != null) {
            switch (publicGetEventsCriteria.getSort()) {
                case EVENT_DATE:
                    strSort = "eventDate";
                    break;
                case VIEWS:
                    strSort = "views";
            }
            cqEvent.orderBy(cb.asc(rootEvent.get(strSort)));
        } else {
            cqEvent.orderBy(cb.asc(rootEvent.get("id")));
        }

        TypedQuery<Event> query = em.createQuery(cqEvent);

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

    private Long getConfirmedRequests(Long eventId) {
        return em.createQuery("SELECT COUNT(p.id) " +
                                "FROM ParticipationRequest p " +
                                "WHERE p.event.id = " + eventId,
                        Long.class)
                .getSingleResult();
    }
}
