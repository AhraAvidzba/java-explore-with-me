package ru.practicum.statistics.statistic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistics.statistic.dtos.StatisticDto;
import ru.practicum.statistics.statistic.dtos.StatisticDtoOut;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RestController
//@RequestMapping(path = "/stats")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public StatisticDto addStatistic(@RequestBody Statistic statistic) {
        log.info("adding statistic");
        return statisticService.addStatistic(statistic);
    }

    @GetMapping("/stats")
    public List<StatisticDtoOut> getStatistics(@RequestParam(name = "start") String strStart,
                                               @RequestParam(name = "end") String strEnd,
                                               @RequestParam(defaultValue = "") String[] uris,
                                               @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Getting statistic");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(strStart, formatter);
        LocalDateTime end = LocalDateTime.parse(strEnd, formatter);

        return statisticService.getStatistics(start, end, uris, unique);
    }
}
