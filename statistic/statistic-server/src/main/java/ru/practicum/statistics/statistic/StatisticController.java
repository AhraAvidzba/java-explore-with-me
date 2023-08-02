package ru.practicum.statistics.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsProjection;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public StatisticOutDto addStatistic(@RequestBody StatisticInDto statisticInDto) {
        log.info("adding statistic");
        return statisticService.addStatistic(statisticInDto);
    }

    @GetMapping("/stats")
    public List<StatisticWithHitsProjection> getStatistics(@RequestParam(name = "start") String strStart,
                                                           @RequestParam(name = "end") String strEnd,
                                                           @RequestParam(defaultValue = "") String[] uris,
                                                           @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Getting statistic");
        strStart = URLDecoder.decode(strStart, StandardCharsets.UTF_8);
        strEnd = URLDecoder.decode(strEnd, StandardCharsets.UTF_8);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(strStart, formatter);
        LocalDateTime end = LocalDateTime.parse(strEnd, formatter);
        List<StatisticWithHitsProjection> res = statisticService.getStatistics(start, end, uris, unique);;
        return res;
    }
}
