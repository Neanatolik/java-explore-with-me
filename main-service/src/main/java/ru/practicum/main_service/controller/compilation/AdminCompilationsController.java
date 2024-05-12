package ru.practicum.main_service.controller.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main_service.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationsController {
    private final CompilationService compilationService;

    public AdminCompilationsController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations");
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long id) {
        log.info("DELETE /compilations/{}", id);
        compilationService.deleteCompilation(id);
    }

    @PatchMapping(path = "/{id}")
    public CompilationDto changeCompilation(@Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable long id) {
        log.info("PATCH /compilations/{}", id);
        return compilationService.changeCompilation(updateCompilationRequest, id);
    }
}
