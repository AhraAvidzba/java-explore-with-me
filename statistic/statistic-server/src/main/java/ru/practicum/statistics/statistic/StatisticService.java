package ru.practicum.statistics.statistic;

import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    StatisticOutDto addStatistic(StatisticInDto statisticInDto);

    List<StatisticWithHitsProjection> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
