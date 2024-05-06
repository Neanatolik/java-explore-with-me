package ru.practicum.stats.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsDtoIn;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ViewStatsMapper {
    public ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }

    public ViewStatsDto fromViewStatsDtoIn(ViewStatsDtoIn viewStats) {
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits()
        );
    }

    public ViewStats fromViewStatsDto(ViewStatsDto viewStatsDto) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(viewStatsDto.getApp());
        viewStats.setUri(viewStatsDto.getUri());
        viewStats.setHits(viewStatsDto.getHits());
        return viewStats;
    }

    public List<ViewStatsDto> toListViewStats(Iterable<ViewStats> viewStatsList) {
        List<ViewStatsDto> dtoList = new ArrayList<>();
        for (ViewStats viewStats : viewStatsList) {
            dtoList.add(toViewStatsDto(viewStats));
        }
        return dtoList;
    }

    public List<ViewStatsDto> mapToListViewStats(Iterable<ViewStatsDtoIn> viewStatsList) {
        List<ViewStatsDto> dtoList = new ArrayList<>();
        for (ViewStatsDtoIn viewStats : viewStatsList) {
            dtoList.add(fromViewStatsDtoIn(viewStats));
        }
        return dtoList;
    }
}
