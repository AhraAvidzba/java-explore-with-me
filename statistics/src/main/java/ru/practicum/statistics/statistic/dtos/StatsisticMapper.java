package ru.practicum.statistics.statistic.dtos;

import ru.practicum.statistics.statistic.Statistic;

public class StatsisticMapper {
    public static StatisticDto mapToStatisticDto(Statistic statistic) {
        StatisticDto statisticDto = new StatisticDto();
        statisticDto.setUri(statistic.getUri());
        statisticDto.setApp(statistic.getApp());
        statisticDto.setHits(3L);
        return statisticDto;
    }

    public static Statistic mapToStatistic(StatisticDto statisticDto) {
        Statistic statistic = new Statistic();
        statistic.setUri(statisticDto.getUri());
        statistic.setApp(statisticDto.getApp());
        return statistic;
    }
}
