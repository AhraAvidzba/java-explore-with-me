package ru.practicum.ewm.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsDto;
import ru.practicum.ewm.dtos.StatisticWithHitsProjection;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticClient statisticClient;

    @PostMapping("/hit")
    public StatisticOutDto addStatistic(@RequestBody StatisticInDto statisticInDto) {
        log.info("adding statistic");
        return statisticClient.addStatistic(statisticInDto);
    }

    @GetMapping("/stats")
    public List<StatisticWithHitsDto> getStatistics(@RequestParam(name = "start") String strStart,
                                                    @RequestParam(name = "end") String strEnd,
                                                    @RequestParam(defaultValue = "") String[] uris,
                                                    @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Getting statistic");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime start = LocalDateTime.parse(strStart, formatter);
//        LocalDateTime end = LocalDateTime.parse(strEnd, formatter);
//
//        strStart = start.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        strEnd = end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return statisticClient.getStatistics(strStart, strEnd, uris, unique);
    }
}
