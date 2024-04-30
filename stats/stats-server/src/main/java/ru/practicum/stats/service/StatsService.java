package ru.practicum.stats.service;

import ru.practicum.dto.ViewStatsDtoIn;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<ViewStatsDtoIn> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique);
}
