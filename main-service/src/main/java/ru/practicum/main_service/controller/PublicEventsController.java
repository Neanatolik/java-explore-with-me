package ru.practicum.main_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.hit.HitClient;
import ru.practicum.main_service.dto.EventFullDto;
import ru.practicum.main_service.dto.EventShortDto;
import ru.practicum.main_service.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/events")
@Slf4j
public class PublicEventsController {
    private final HitClient hitClient;
    private final EventService eventService;

    @Autowired
    public PublicEventsController(HitClient hitClient, EventService eventService) {
        this.hitClient = hitClient;
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEventsByParameters(@RequestParam(required = false) String text,
                                                     @RequestParam(required = false) List<@Positive Integer> categories,
                                                     @RequestParam(required = false) Boolean paid,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                                     @RequestParam(required = false) String sort,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                     @Positive @RequestParam(defaultValue = "10") int size,
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

    @GetMapping(path = "/{id}")
    public EventFullDto getEventsByUserId(@PathVariable long id,
                                          HttpServletRequest request) {
        log.info("GET /events/{}\nip: {}\npath: {}", id, request.getRemoteAddr(), request.getRequestURI());
        hitClient.saveHit(new EndpointHitDto("MainService", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        return eventService.getEventByIdPublic(id);
    }
}
