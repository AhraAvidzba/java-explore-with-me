package ru.practicum.ewm.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.Event;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull
    private List<Event> events;
    @Column
    @NotNull
    private Boolean pinned;
    @Column
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
