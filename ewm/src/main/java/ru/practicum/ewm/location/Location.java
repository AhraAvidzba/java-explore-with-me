package ru.practicum.ewm.location;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "locations")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private float lat; //широта
    @Column
    private float lon; //долгота
}
