package ru.practicum.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.hit.model.HitMapperMS;
import ru.practicum.hit.repository.HitRepository;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;
    private final HitMapperMS hitMapperMS;

    @Override
    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHit) {
        return hitMapperMS.toEndpointHitDto(hitRepository.save(hitMapperMS.fromEndpointHitDto(endpointHit)));
    }
}
