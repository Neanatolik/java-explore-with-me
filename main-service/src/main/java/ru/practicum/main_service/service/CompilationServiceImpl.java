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

import java.util.*;

@Service
@Transactional
public class CompilationServiceImpl implements CompilationService{
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        checkNewCompilationDto(newCompilationDto);
        return CompilationMapper.toCompilationDto(compilationRepository.save(CompilationMapper.fromNewCompilationDto(newCompilationDto, getSetOfEvents(newCompilationDto.getEvents()))));
    }

    @Override
    public void deleteCompilation(long id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto changeCompilation(UpdateCompilationRequest updateCompilationRequest, long id) {
        System.out.println("updateCompilationRequest: " + updateCompilationRequest);
        return CompilationMapper.toCompilationDto(compilationRepository.save(updateCompilation(compilationRepository.getReferenceById(id), updateCompilationRequest)));
    }

    private Compilation updateCompilation(Compilation compilation, UpdateCompilationRequest updateCompilationRequest) {
        if (Objects.nonNull(updateCompilationRequest.getTitle())) {
            if (updateCompilationRequest.getTitle().length() > 50 || updateCompilationRequest.getTitle().isBlank()) {
                throw new BadRequest("", "");
            }
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        if (Objects.nonNull(updateCompilationRequest.getPinned())) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (Objects.nonNull(updateCompilationRequest.getEvents())) {
            compilation.setEvent(getSetOfEvents(updateCompilationRequest.getEvents()));
        }
        System.out.println(compilation);
        System.out.println(updateCompilationRequest.getEvents());
        return compilation;
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return CompilationMapper.mapToCompilationDto(compilationRepository.getCompilations(pinned, page));
    }

    @Override
    public CompilationDto getCompilationById(long id) {
        Optional<Compilation> compilation = compilationRepository.findById(id);
        if (compilation.isEmpty()) {
            throw new NotFoundException("", "");
        }
        return CompilationMapper.toCompilationDto(compilation.get());
    }

    private Set<Event> getSetOfEvents(Set<Long> event) {
        Set<Event> events = new HashSet<>();
        if (Objects.isNull(event)) return events;
        for(Long eventId : event) {
            System.out.println("Event: " + event);
            events.add(eventRepository.getReferenceById(eventId));
        }
        return events;
    }

    private void checkNewCompilationDto(NewCompilationDto newCompilationDto) {
        if (Objects.isNull(newCompilationDto.getTitle()) || newCompilationDto.getTitle().isBlank()) {
            throw new BadRequest("", "");
        }
        if (newCompilationDto.getTitle().length()>50) {
            throw new BadRequest("", "");
        }
    }
}
