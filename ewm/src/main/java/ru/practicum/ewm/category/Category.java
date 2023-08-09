package ru.practicum.ewm.category;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "categories")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotEmpty
    private String name;
}
