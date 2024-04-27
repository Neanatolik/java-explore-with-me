package ru.practicum.hit.model;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.EndpointHitDto;

@UtilityClass
public class HitMapper {
    public EndpointHitDto toEndpointHitDto(Hit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public Hit fromEndpointHitDto(EndpointHitDto endpointHitDto) {
        Hit hit = new Hit();
        hit.setApp(endpointHitDto.getApp());
        hit.setUri(endpointHitDto.getUri());
        hit.setIp(endpointHitDto.getIp());
        hit.setTimestamp(endpointHitDto.getTimestamp());
        return hit;
    }
}
