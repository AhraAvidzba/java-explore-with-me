package ru.practicum.statistics.statistic.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDto {
    private String app;
    private String uri;
    private Long hits;

//    public StatisticDto(String app, String uri, Long hits) {
//        this.app = app;
//        this.uri = uri;
//        this.hits = hits;
//    }
}
