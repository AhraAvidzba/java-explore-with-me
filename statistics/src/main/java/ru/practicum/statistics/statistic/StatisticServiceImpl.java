package ru.practicum.statistics.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.statistics.statistic.dtos.StatisticDto;
import ru.practicum.statistics.statistic.dtos.StatisticDtoOut;
import ru.practicum.statistics.statistic.dtos.StatsisticMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final StatisticRepository statisticRepository;
    @Override
    public StatisticDto addStatistic(Statistic statistic) {
        return StatsisticMapper.mapToStatisticDto(statisticRepository.save(statistic));
    }

    @Override
    public List<StatisticDtoOut> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
//        String strStart = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        String strEnd = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//        start = LocalDateTime.parse("2020-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        end = LocalDateTime.parse("2024-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<StatisticDtoOut> statistics;
        if (uris.length == 0 && unique) {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
        } else if (uris.length > 0 && unique) {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
        } else if (uris.length == 0) {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
        } else {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
        }
        return statistics; //.stream()
//                .map(StatsisticMapper::mapToStatisticDto)
//                .collect(Collectors.toList());
    }
}
