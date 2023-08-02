package ru.practicum.ewm.statistic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.client.BaseClient;
import ru.practicum.ewm.dtos.StatisticInDto;
import ru.practicum.ewm.dtos.StatisticOutDto;
import ru.practicum.ewm.dtos.StatisticWithHitsDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.RequestEntity.post;


@Service
public class StatisticClient extends BaseClient {

    private static final String API_PREFIX = "";

    private ObjectMapper objectMapper;
    @Autowired
    public StatisticClient(@Value("${statistics-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
//        this.objectMapper = objectMapper;
    }

    public StatisticOutDto addStatistic(StatisticInDto statistic) {
        String statisticOutDtoStr = post("/hit", statistic).getBody();
        StatisticOutDto statisticOutDto;
        try {
            statisticOutDto = objectMapper.readValue(statisticOutDtoStr, StatisticOutDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return statisticOutDto;
    }

    public List<StatisticWithHitsDto> getStatistics(String strStart, String strEnd, String[] uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "strStart", strStart,
                "strEnd", strEnd,
                "uris", uris,
                "unique", unique
        );
        String statisticOutDtoListStr = get("?strStart={strStart}&strEnd={strEnd}&unique={unique}&uris={uris}", parameters).getBody();
        List<StatisticWithHitsDto> statisticOutDtoList;
        try {
            statisticOutDtoList = Arrays.stream(objectMapper.readValue(statisticOutDtoListStr, StatisticWithHitsDto[].class)).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return statisticOutDtoList;
    }
}
