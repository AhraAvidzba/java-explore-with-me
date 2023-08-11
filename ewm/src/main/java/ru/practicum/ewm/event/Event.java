package ru.practicum.ewm.event;

import lombok.*;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column
    private int confirmedRequests;
    @Column
    private LocalDateTime createdOn;
    @Column
    private String description;
    @Column
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locaton_id")
    private Location location;
    @Column
    private boolean paid;
    @Column
    private int participantLimit;
    @Column
    private LocalDateTime publishedOn;
    @Column
    private boolean requestModeration = true;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column
    private String title;
    @Column
    private int views;
}
