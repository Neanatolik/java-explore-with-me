package ru.practicum.hit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.hit.model.HitMapper;
import ru.practicum.hit.repository.HitRepository;

@Service
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;


    public HitServiceImpl(HitRepository hitRepository) {
        this.hitRepository = hitRepository;
    }

    @Override
    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHit) {
        return HitMapper.toEndpointHitDto(hitRepository.save(HitMapper.fromEndpointHitDto(endpointHit)));
    }
}
