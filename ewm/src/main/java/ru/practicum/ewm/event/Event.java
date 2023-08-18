package ru.practicum.ewm.event;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.category.Category;
import ru.practicum.ewm.location.Location;
import ru.practicum.ewm.request.ParticipationRequest;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import javax.persistence.metamodel.StaticMetamodel;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@StaticMetamodel(Event.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @NotNull
    private Category category;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private List<ParticipationRequest> confirmedRequests;
    @Column
    private LocalDateTime createdOn;
    @Column
    @Length(min = 20, max = 7000)
    private String description;
    @Column
    @NotNull
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locaton_id")
    @NotNull
    private Location location;
    @Column
    @NotNull
    private Boolean paid;
    @Column
    private Integer participantLimit;
    @Column
    private LocalDateTime publishedOn;
    @Column
    private boolean requestModeration = true;
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;
    @Column
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
    @Column
    private int views;
}
