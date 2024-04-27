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
        System.out.println("WHAT: " + Objects.isNull(uris));
        if (Objects.nonNull(uris)) {
            System.out.println("URI: " + String.join(" ", uris));
            if (unique) {
                System.out.println("PAR3: " + start + " " + end);
                viewStatsDtos = hitRepository.countUnique(start, end, uris);
            } else {
                viewStatsDtos = hitRepository.countUnique2(start, end, uris);
            }
        } else {
            if (unique) {
                viewStatsDtos = hitRepository.countFromStartToEndByNotUnique(start, end);
            } else {
                viewStatsDtos = hitRepository.countFromStartToEndByNotUnique(start, end);
            }
        }
        return viewStatsDtos;
    }
}
