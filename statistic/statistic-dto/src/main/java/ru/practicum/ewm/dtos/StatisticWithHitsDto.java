package ru.practicum.ewm.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticWithHitsDto {
    private String app;
    private String uri;
    private Long hits;
}
