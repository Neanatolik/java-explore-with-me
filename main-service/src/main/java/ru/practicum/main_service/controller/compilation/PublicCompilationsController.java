package ru.practicum.main_service.controller.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.CompilationDto;
import ru.practicum.main_service.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Validated
@Slf4j
public class PublicCompilationsController {
    private final CompilationService compilationService;

    public PublicCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "{id}")
    public CompilationDto getCompilationById(@PathVariable long id) {
        log.info("GET /compilations/{}", id);
        return compilationService.getCompilationById(id);
    }
}
