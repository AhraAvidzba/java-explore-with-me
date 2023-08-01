package ru.practicum.statistics.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService{
    private final StatisticRepository statisticRepository;
    @Override
    public StatisticOutDto addStatistic(StatisticInDto statisticInDto) {
        Statistic statistic = statisticRepository.save(StatisticMapper.mapToStatistic(statisticInDto));
        return StatisticMapper.mapToStatisticOutDto(statistic);
    }

    @Override
    public List<StatisticWithHitsDto> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<StatisticWithHitsDto> statistics;
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
