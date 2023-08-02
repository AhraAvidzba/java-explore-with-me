package ru.practicum.statistics.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dtos.StatisticWithHitsProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("select s.app as app, s.uri as uri, count(distinct s.ip) as hits " +
            "from Statistic as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.uri, s.app " +
            "order by hits desc ")
    List<StatisticWithHitsProjection> findStatisticByTimeAndUniqueIp(LocalDateTime  start, LocalDateTime end);

    @Query("select s.app as app, s.uri as uri, count(distinct s.ip) as hits " +
            "from Statistic as s " +
            "where (s.timestamp between ?1 and ?2) " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by hits desc ")
    List<StatisticWithHitsProjection> findStatisticByTimeAndUniqueIpAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
    @Query("select s.app as app, s.uri as uri, count(s.ip) as hits " +
            "from Statistic as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.uri, s.app " +
            "order by hits desc ")
    List<StatisticWithHitsProjection> findStatisticByTime(LocalDateTime start, LocalDateTime end);
    @Query("select s.app as app, s.uri as uri, count(s.ip) as hits " +
            "from Statistic as s " +
            "where (s.timestamp between ?1 and ?2) " +
            "and s.uri in ?3 " +
            "group by s.uri, s.app " +
            "order by hits desc ")
    List<StatisticWithHitsProjection> findStatisticByTimeAndAndUris(LocalDateTime start, LocalDateTime end, String[] uris);
}
