package ru.practicum.statistics.statistic;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "statistic")
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotEmpty
    private String app;
    @Column
    @NotEmpty
    private String uri;
    @Column
    private String ip;
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
}
