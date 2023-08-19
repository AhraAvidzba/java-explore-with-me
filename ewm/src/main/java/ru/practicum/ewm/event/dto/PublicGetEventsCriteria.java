package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.Sort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicGetEventsCriteria {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private String rangeStart;
    private String rangeEnd;
    private Boolean onlyAvailable = false;
    private Sort sort;
    @PositiveOrZero
    private int from = 0;
    @Positive
    private int size = 10;
}
