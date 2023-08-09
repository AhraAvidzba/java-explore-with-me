package ru.practicum.ewm.compilation;

import lombok.*;
import ru.practicum.ewm.event.Event;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "compilations")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    private List<Event> events;
    @Column
    private boolean pinned;
    @Column
    private String title;
}
