package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.CompilationDto;
import ru.practicum.main_service.dto.NewCompilationDto;
import ru.practicum.main_service.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long id);

    CompilationDto changeCompilation(UpdateCompilationRequest updateCompilationRequest, long id);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilationById(long id);
}
