package ru.practicum.main_service.controller.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.EventShortDto;
import ru.practicum.main_service.dto.event.NewEventDto;
import ru.practicum.main_service.dto.event.UpdateEventUserRequest;
import ru.practicum.main_service.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{id}/events")
@Validated
@Slf4j
public class PrivateEventsController {
    private final EventService eventService;

    @Autowired
    public PrivateEventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@Positive @PathVariable long id,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                 @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/events", id);
        return eventService.getEventsByUserId(id, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@Positive @PathVariable long id,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events", id);
        return eventService.saveEvent(id, newEventDto);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventById(@Positive @PathVariable long id,
                                     @Positive @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}", id, eventId);
        return eventService.getEventById(id, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto changeEventById(@Positive @PathVariable long id,
                                        @Positive @PathVariable long eventId,
                                        @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH /users/{}/events/{}", id, eventId);
        return eventService.changeEventById(id, eventId, updateEventUserRequest);
    }

}
