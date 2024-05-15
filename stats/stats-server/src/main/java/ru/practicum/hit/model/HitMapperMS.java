package ru.practicum.hit.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;

import static ru.practicum.StatsServer.DATE_FORMAT;

@Mapper(componentModel = "spring")
public interface HitMapperMS {

    @Mapping(target = "timestamp", source = "hit.timestamp", dateFormat = DATE_FORMAT)
    EndpointHitDto toEndpointHitDto(Hit hit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", source = "endpointHitDto.timestamp", dateFormat = DATE_FORMAT)
    Hit fromEndpointHitDto(EndpointHitDto endpointHitDto);

}
