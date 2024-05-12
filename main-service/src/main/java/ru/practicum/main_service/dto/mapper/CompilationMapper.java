package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.model.Compilation;
import ru.practicum.main_service.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class CompilationMapper {
    public CompilationDto toCompilationDto(Compilation compilation, Map<Long, Integer> views) {

        return new CompilationDto(compilation.getId(), EventMapper.mapToEventShortDtoSet(compilation.getEvent(), views), compilation.getPinned(), compilation.getTitle());
    }

    public static Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvent(events);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static List<CompilationDto> mapToCompilationDto(List<Compilation> compilations) {
        List<CompilationDto> dtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            dtoList.add(toCompilationDto(compilation, null));
        }
        return dtoList;
    }
}
