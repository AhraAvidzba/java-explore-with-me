package ru.practicum.ewm.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l " +
            "from Location as l " +
            "where ABS(l.lat - ?1) < ?3 " +
            "and ABS(l.lon - ?2) < ?3 ")
    Optional<Location> findLocationByLatAndLon(float lat, float lon, float accuracy);
}
