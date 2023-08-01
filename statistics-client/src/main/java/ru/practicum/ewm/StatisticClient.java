package ru.practicum.ewm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.dtos.StatisticInDto;

import java.util.Map;

@Service
public class StatisticClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public StatisticClient(@Value("${statistics-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addStatistic(StatisticInDto statistic) {
        return post("/hit", statistic);
    }

    public ResponseEntity<Object> getStatistics(String strStart, String strEnd, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "strStart", strStart,
                "strEnd", strEnd,
                "uris", uris,
                "unique", unique
        );
        return get("?strStart={strStart}&strEnd={strEnd}&unique={unique}&uris={uris}", parameters);
    }
}
