package ru.practicum.ewm.event;

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
    private float lat; //ширина
    @Column
    private float lon; //долгота
}
