package ru.practicum.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.CompilationDto;
import ru.practicum.main_service.dto.NewCompilationDto;
import ru.practicum.main_service.dto.UpdateCompilationRequest;
import ru.practicum.main_service.dto.mapper.CompilationMapper;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Compilation;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.repository.CompilationRepository;
import ru.practicum.main_service.repository.EventRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        return CompilationMapper.toCompilationDto(compilationRepository.save(CompilationMapper.fromNewCompilationDto(newCompilationDto, getSetOfEvents(newCompilationDto.getEvents()))), null);
    }

    @Override
    public void deleteCompilation(long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto changeCompilation(UpdateCompilationRequest updateCompilationRequest, long id) {
        return CompilationMapper.toCompilationDto(compilationRepository.save(updateCompilation(compilationRepository.getReferenceById(id), updateCompilationRequest)), null);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return CompilationMapper.mapToCompilationDto(compilationRepository.getCompilations(pinned, page));
    }

    @Override
    public CompilationDto getCompilationById(long id) {
        return CompilationMapper.toCompilationDto(compilationRepository.findById(id).orElseThrow(() -> new NotFoundException("Данный compilation не найден", "Ошибка запроса")), null);
    }

    private Compilation updateCompilation(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (Objects.nonNull(updateCompilationRequest.getTitle())) {
            if (updateCompilationRequest.getTitle().isBlank()) {
                throw new BadRequest("Пустой текст", "Ошибка обновления данных");
            }
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (Objects.nonNull(updateCompilationRequest.getPinned())) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (Objects.nonNull(updateCompilationRequest.getEvents())) {
            compilation.setEvent(getSetOfEvents(updateCompilationRequest.getEvents()));
        }
        return compilation;
    }

    private Set<Event> getSetOfEvents(Set<Long> event) {
        if (Objects.isNull(event)) return new HashSet<>();
        return eventRepository.getByListOfId(event);
    }
}
