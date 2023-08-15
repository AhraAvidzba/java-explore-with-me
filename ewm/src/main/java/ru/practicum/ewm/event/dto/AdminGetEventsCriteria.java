package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.State;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminGetEventsCriteria {
    @NotNull
    private List<Long> users;
    @NotNull
    private List<State> states;
    @NotNull
    private List<Long> categories;
    private String rangeStart;
    private String rangeEnd;
    @PositiveOrZero
    private int from = 0;
    @Positive
    private int size = 10;
}
