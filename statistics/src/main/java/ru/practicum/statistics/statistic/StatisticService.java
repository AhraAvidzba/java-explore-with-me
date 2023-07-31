package ru.practicum.statistics.statistic;

import ru.practicum.statistics.statistic.dtos.StatisticDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    StatisticDto addStatistic(Statistic statistic);
    List<StatisticDto> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
