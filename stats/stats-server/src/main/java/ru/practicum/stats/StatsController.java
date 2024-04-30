package ru.practicum.stats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ViewStatsDtoIn;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/stats")
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public List<ViewStatsDtoIn> getHits(@RequestParam(required = false) List<String> uris,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats?uris={}&start={}&end={}&unique={}", Objects.nonNull(uris) ? String.join("&uris", uris) : "", start, end, unique);
        return statsService.getStats(uris, start, end, unique);
    }

}
