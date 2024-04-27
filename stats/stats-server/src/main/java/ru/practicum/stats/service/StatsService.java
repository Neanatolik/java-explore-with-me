package ru.practicum.stats.service;

import ru.practicum.dto.ViewStatsDtoIn;

import java.util.List;

public interface StatsService {
    List<ViewStatsDtoIn> getStats(List<String> uris, String start, String end, boolean unique);
}
