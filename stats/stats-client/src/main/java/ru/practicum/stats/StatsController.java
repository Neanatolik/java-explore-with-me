package ru.practicum.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/stats")
@Validated
@Slf4j
public class StatsController {
    private final StatsClient statsClient;

    @Autowired
    public StatsController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @GetMapping
    public ResponseEntity<Object> getHits(@RequestParam(required = false) List<String> uris,
                                          @RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats?uris={}&start={}&end={}&unique={}", Objects.nonNull(uris) ? String.join("&uris", uris) : "", start, end, unique);
        return statsClient.getHits(uris, start, end, unique);
    }
}
