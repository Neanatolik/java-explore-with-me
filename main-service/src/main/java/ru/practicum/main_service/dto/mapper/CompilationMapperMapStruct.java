package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.model.Compilation;
import ru.practicum.main_service.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring")

public interface CompilationMapperMapStruct {

    EventMapperMapStruct eventMapperMapStruct = new EventMapperMapStructImpl();

    default CompilationDto toCompilationDto(Compilation compilation, Map<Long, Integer> views) {
        return new CompilationDto(compilation.getId(), eventMapperMapStruct.mapToEventShortDtoSet(compilation.getEvent(), views), compilation.getPinned(), compilation.getTitle());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "event", source = "events")
    Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto, Set<Event> events);

    default List<CompilationDto> mapToCompilationDto(List<Compilation> compilations) {
        List<CompilationDto> dtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            dtoList.add(toCompilationDto(compilation, null));
        }
        return dtoList;
    }

}
