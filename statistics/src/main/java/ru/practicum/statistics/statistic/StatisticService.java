package ru.practicum.statistics.statistic;

import ru.practicum.statistics.statistic.dtos.StatisticDto;
import ru.practicum.statistics.statistic.dtos.StatisticDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    StatisticDto addStatistic(Statistic statistic);
    List<StatisticDtoOut> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
