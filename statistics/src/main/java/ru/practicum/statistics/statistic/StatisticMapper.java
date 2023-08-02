package ru.practicum.statistics.statistic;

import ru.practicum.statistics.statistic.Statistic;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;

public class StatisticMapper {
    public static Statistic mapToStatistic(StatisticInDto statisticInDto) {
        Statistic statistic = new Statistic();
        statistic.setUri(statisticInDto.getUri());
        statistic.setApp(statisticInDto.getApp());
        statistic.setIp(statisticInDto.getIp());
        statistic.setTimestamp(statisticInDto.getTimestamp());
        return statistic;
    }

    public static StatisticOutDto mapToStatisticOutDto(Statistic statistic) {
        StatisticOutDto statisticOutDto = new StatisticOutDto();
        statisticOutDto.setId(statistic.getId());
        statisticOutDto.setUri(statistic.getUri());
        statisticOutDto.setApp(statistic.getApp());
        statisticOutDto.setIp(statistic.getIp());
        statisticOutDto.setTimestamp(statistic.getTimestamp());
        return statisticOutDto;
    }
}
