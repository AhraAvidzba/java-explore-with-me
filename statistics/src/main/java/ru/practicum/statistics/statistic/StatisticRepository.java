package ru.practicum.statistics.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dtos.StatisticWithHitsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("select s.app as app, s.uri as uri, count(distinct s.ip) as hits " +
            "from Statistic as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.uri " +
            "order by hits desc ")
    List<StatisticWithHitsDto> findStatisticByTimeAndUniqueIp(LocalDateTime  start, LocalDateTime end);

    @Query("select s.app as app, s.uri as uri, count(distinct s.ip) as hits " +
            "from Statistic as s " +
            "where (s.timestamp between ?1 and ?2) " +
            "and s.uri in ?3 " +
            "group by s.uri " +
            "order by hits desc ")
    List<StatisticWithHitsDto> findStatisticByTimeAndUniqueIpAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
    @Query("select s.app as app, s.uri as uri, count(s.ip) as hits " +
            "from Statistic as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.uri " +
            "order by hits desc ")
    List<StatisticWithHitsDto> findStatisticByTime(LocalDateTime start, LocalDateTime end);
    @Query("select s.app as app, s.uri as uri, count(s.ip) as hits " +
            "from Statistic as s " +
            "where (s.timestamp between ?1 and ?2) " +
            "and s.uri in ?3 " +
            "group by s.uri " +
            "order by hits desc ")
    List<StatisticWithHitsDto> findStatisticByTimeAndAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
}
