package ru.practicum.ewm.request;

import lombok.*;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "requests")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private Status status;
}
