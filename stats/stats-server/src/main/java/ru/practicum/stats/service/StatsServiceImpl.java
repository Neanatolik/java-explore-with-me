package ru.practicum.stats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsDtoIn;
import ru.practicum.hit.repository.HitRepository;

import java.util.List;
import java.util.Objects;

@Service
public class StatsServiceImpl implements StatsService {
    private final HitRepository hitRepository;

    @Autowired
    public StatsServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    public List<ViewStatsDtoIn> getStats(List<String> uris, String start, String end, boolean unique) {
        List<ViewStatsDtoIn> viewStatsDtos;
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
        return viewStatsDtos;
    }
}
