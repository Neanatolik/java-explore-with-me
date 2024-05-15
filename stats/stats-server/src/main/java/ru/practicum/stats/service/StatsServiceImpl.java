package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.ViewStatsDtoIn;
import ru.practicum.exceptions.BadRequest;
import ru.practicum.hit.repository.HitRepository;
import ru.practicum.stats.model.ViewStatsMapperMS;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final HitRepository hitRepository;
    private final ViewStatsMapperMS viewStatsMapperMS;

    @Override
    public List<ViewStatsDto> getStats(List<String> uris, LocalDateTime start, LocalDateTime end, boolean unique) {
        List<ViewStatsDtoIn> viewStatsDtos;
        checkDates(start, end);
        if (Objects.nonNull(uris)) {
            if (unique) {
                viewStatsDtos = hitRepository.countUniqueByUris(start, end, uris);
            } else {
                viewStatsDtos = hitRepository.countNonUniqueByUris(start, end, uris);
            }
        } else {
            if (unique) {
                viewStatsDtos = hitRepository.countUniqueWithoutUris(start, end);
            } else {
                viewStatsDtos = hitRepository.countNonUniqueWithoutUris(start, end);
            }
        }
        return viewStatsMapperMS.mapToListViewStats(viewStatsDtos);
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (Objects.isNull(start) || Objects.isNull(end)) {
            throw new BadRequest("Отсутствуют даты start или end", "Ошибка запроса");
        }
        if (start.isAfter(end)) {
            throw new BadRequest("start позже чем end", "Ошибка запроса");
        }
    }


}
