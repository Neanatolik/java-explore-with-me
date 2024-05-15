package ru.practicum.stats.model;

import org.mapstruct.Mapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsDtoIn;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapperMS {

    ViewStatsDto fromViewStatsDtoIn(ViewStatsDtoIn viewStats);

    List<ViewStatsDto> mapToListViewStats(Iterable<ViewStatsDtoIn> viewStatsList);
}
