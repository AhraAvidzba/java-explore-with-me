package ru.practicum.statistics.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsProjection;
import ru.practicum.statistics.exceptions.NotValidDatesException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    @Override
    public StatisticOutDto addStatistic(StatisticInDto statisticInDto) {
        Statistic statistic = statisticRepository.save(StatisticMapper.mapToStatistic(statisticInDto));
        return StatisticMapper.mapToStatisticOutDto(statistic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticWithHitsProjection> getStatistics(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new NotValidDatesException("Дата начала не может быть позже даты конца");
        }
        List<StatisticWithHitsProjection> statistics;
        if (unique) {
            if (uris.length == 0) {
                statistics = statisticRepository.findStatisticByTimeAndUniqueIp(start, end);
            } else {
                statistics = statisticRepository.findStatisticByTimeAndUniqueIpAndUris(start, end, uris);
            }
        } else {
            if (uris.length == 0) {
                statistics = statisticRepository.findStatisticByTime(start, end);
            } else {
                statistics = statisticRepository.findStatisticByTimeAndAndUris(start, end, uris);
            }
        }
        return statistics;
    }
}
