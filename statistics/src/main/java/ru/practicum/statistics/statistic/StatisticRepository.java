package ru.practicum.statistics.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.statistics.statistic.dtos.StatisticDto;

import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("select new ru.practicum.statistics.statistic.dtos.StatisticDto(s.app, s.uri, count(s.id)) " +
            "from Statistic as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.uri, s.ip ")
    List<StatisticDto> findStatisticByTimeAndUniqueIp(LocalDateTime  start, LocalDateTime end);
//    @Query()
//    List<Statistic> findStatisticByTimeAndUniqueIpAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
//    @Query()
//    List<Statistic> findStatisticByTime(LocalDateTime start, LocalDateTime end);
//    @Query()
//    List<Statistic> findStatisticByTimeAndAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
}
