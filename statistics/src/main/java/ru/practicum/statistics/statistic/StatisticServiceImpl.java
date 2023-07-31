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
    public Statistic addStatistic(Statistic statistic) {
        return statisticRepository.save(statistic);
    }

    @Override
    public List<StatisticDtoOut> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<StatisticDtoOut> statistics;
        if (uris.length == 0 && unique) {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
        } else if (uris.length > 0 && unique) {
            statistics = statisticRepository.findStatisticByTimeAndUniqueIpAndUris(start, end, uris);
        } else if (uris.length == 0) {
            statistics = statisticRepository.findStatisticByTime(start, end);
        } else {
            statistics = statisticRepository.findStatisticByTimeAndAndUris(start, end, uris);
        }
        return statistics;
    }
}
