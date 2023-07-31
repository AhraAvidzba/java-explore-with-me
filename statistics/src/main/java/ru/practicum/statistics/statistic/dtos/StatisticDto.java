package ru.practicum.statistics.statistic.dtos;

import lombok.*;

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
