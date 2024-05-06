package ru.practicum.main_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.*;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.service.EventService;
import ru.practicum.main_service.service.RequestService;
import ru.practicum.main_service.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @Autowired
    public PrivateController(UserService userService, EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @GetMapping(path = "/{id}/events")
    public List<EventShortDto> getEventsByUserId(@PathVariable long id, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/events", id);
        return eventService.getEventsByUserId(id, from, size);
    }

    @PostMapping(path = "/{id}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto getEventsByUserId(@PathVariable long id, @RequestBody NewEventDto newEventDto) {
        log.info("POST /users/{}/events", id);
        return eventService.saveEvent(id, newEventDto);
    }

    @GetMapping(path = "/{id}/events/{eventId}")
    public EventFullDto getEventsByUserId(@PathVariable long id, @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}", id, eventId);
        return eventService.getEventById(id, eventId);
    }

    @PatchMapping(path = "/{id}/events/{eventId}")
    public EventFullDto changeEventById(@PathVariable long id, @PathVariable long eventId, @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("PATCH /users/{}/events/{}", id, eventId);
        return eventService.changeEventById(id, eventId, updateEventUserRequest);
    }

    @GetMapping(path = "/{id}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestById(@PathVariable long id, @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}/requests", id, eventId);
        return requestService.getRequestById(id, eventId);
    }

    @PatchMapping(path = "/{id}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestById(@PathVariable long id,
                                                            @PathVariable long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH /users/{}/events/{}/requests", id, eventId);
        return requestService.changeRequestById(id, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping(path = "/{id}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable long id) {
        log.info("GET /users/{}/requests", id);
        return requestService.getRequests(id);
    }

    @PostMapping(path = "/{id}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveRequest(@PathVariable long id, @RequestParam Optional<Long> eventId) {
        log.info("POST /users/{}/requests?eventid={}", id, eventId.isPresent()?eventId.get():"null");
        return requestService.saveRequest(id, eventId.orElseThrow(() -> new BadRequest("Введите id события", "id события должно быть больше 0")));
    }

    @PatchMapping(path = "/{id}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long id, @PathVariable long requestId) {
        log.info("PATCH /users/{}/request/{}/cancel", id, requestId);
        return requestService.cancelRequest(id, requestId);
    }

}
