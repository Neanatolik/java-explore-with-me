package ru.practicum.stats.service;

import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
