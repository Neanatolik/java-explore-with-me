package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.dto.request.ParticipationRequestDto;
import ru.practicum.main_service.model.Request;

import java.util.List;

import static ru.practicum.MainService.DATE_FORMAT;

@Mapper(componentModel = "spring")
public interface RequestMapperMapStruct {

    @Mapping(target = "created", source = "source.created", dateFormat = DATE_FORMAT)
    @Mapping(target = "event", source = "source.event.id")
    @Mapping(target = "requester", source = "source.requester.id")
    ParticipationRequestDto toParticipationRequestDto(Request source);

    List<ParticipationRequestDto> mapToParticipationRequestDto(List<Request> requests);
}
