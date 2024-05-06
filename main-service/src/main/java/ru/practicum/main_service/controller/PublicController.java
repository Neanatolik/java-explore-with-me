package ru.practicum.main_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.hit.HitClient;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.dto.CompilationDto;
import ru.practicum.main_service.dto.EventFullDto;
import ru.practicum.main_service.dto.EventShortDto;
import ru.practicum.main_service.service.CategoryService;
import ru.practicum.main_service.service.CompilationService;
import ru.practicum.main_service.service.EventService;
import ru.practicum.main_service.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/")
@Slf4j
public class PublicController {
    private final HitClient hitClient;
    private final CompilationService compilationService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @Autowired
    public PublicController(UserService userService, HitClient hitClient, CompilationService compilationService, CategoryService categoryService, EventService eventService) {
        this.hitClient = hitClient;
        this.compilationService = compilationService;
        this.categoryService = categoryService;
        this.eventService = eventService;
    }

    @GetMapping(path = "/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/compilations/{id}")
    public CompilationDto getCompilationById(@PathVariable long id) {
        log.info("GET /compilations/{}", id);
        return compilationService.getCompilationById(id);
    }

    @GetMapping(path = "/categories")
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping(path = "/categories/{id}")
    public CategoryDto getCategoriesById(@PathVariable long id) {
        log.info("GET /categories/{}", id);
        return categoryService.getCategoriesById(id);
    }

    @GetMapping(path = "/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size,
                                         HttpServletRequest request) {
        log.info("GET /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort{}&from={}&size={}",
                Objects.nonNull(text) ? text : "null",
                Objects.nonNull(categories) ? StringUtils.join("&categories=", categories) : "null",
                Objects.nonNull(paid) ? paid : "null",
                Objects.nonNull(rangeStart) ? rangeStart : "null",
                Objects.nonNull(rangeEnd) ? rangeEnd : "null",
                onlyAvailable,
                Objects.nonNull(sort) ? sort : "null",
                from,
                size
        );
        hitClient.saveHit(new EndpointHitDto("MainService", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return eventService.getEventsByParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(path = "/events/{id}")
    public EventFullDto getEventsByUserId(@PathVariable long id,
                                          HttpServletRequest request) {
        log.info("GET /events/{}\nip: {}\npath: {}", id, request.getRemoteAddr(), request.getRequestURI());
        hitClient.saveHit(new EndpointHitDto("MainService", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return eventService.getEventByIdPublic(id);
    }
}
