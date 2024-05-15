package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static ru.practicum.StatsServer.DATE_FORMAT;

@RestController
@RequestMapping(path = "/stats")
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public List<ViewStatsDto> getHits(@RequestParam(required = false) List<String> uris,
                                      @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
                                      @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
                                      @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GET /stats?uris={}&start={}&end={}&unique={}", Objects.nonNull(uris) ? String.join("&uris", uris) : "", start, end, unique);
        return statsService.getStats(uris, start, end, unique);
    }

}
