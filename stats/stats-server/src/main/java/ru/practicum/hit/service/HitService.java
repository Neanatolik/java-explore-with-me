package ru.practicum.hit.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;

public interface HitService {

    @Transactional
    EndpointHitDto saveHit(EndpointHitDto endpointHit);
}
