package ru.practicum.statistics.statistic.dtos;

import ru.practicum.statistics.statistic.Statistic;

public class StatsisticMapper {

    public static Statistic mapToStatistic(StatisticDto statisticDto) {
        Statistic statistic = new Statistic();
        statistic.setUri(statisticDto.getUri());
        statistic.setApp(statisticDto.getApp());
        return statistic;
    }
}
