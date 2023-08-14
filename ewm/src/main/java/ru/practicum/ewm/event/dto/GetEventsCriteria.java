package ru.practicum.ewm.event.dto;

import lombok.*;
import ru.practicum.ewm.event.Sort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsCriteria {
    @NotBlank
    private String text;
    @NotNull
    private List<Long> categories;
    private Boolean paid;
    @NotNull
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime rangeStart; // = LocalDateTime.now().minusDays(10000);
    private String rangeStart;
    @NotNull
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime rangeEnd; // = LocalDateTime.now().plusDays(10000);
    private String rangeEnd;
    private Boolean onlyAvailable = false;
    @NotNull
    private Sort sort;
    @PositiveOrZero
    private int from = 0;
    @Positive
    private int size = 10;
}
