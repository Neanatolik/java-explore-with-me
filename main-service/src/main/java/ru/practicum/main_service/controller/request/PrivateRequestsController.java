package ru.practicum.main_service.controller.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.main_service.dto.request.ParticipationRequestDto;
import ru.practicum.main_service.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{id}")
@Validated
@Slf4j
public class PrivateRequestsController {
    private final RequestService requestService;

    public PrivateRequestsController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(path = "/requests")
    public List<ParticipationRequestDto> getRequests(@Positive @PathVariable long id) {
        log.info("GET /users/{}/requests", id);
        return requestService.getRequests(id);
    }

    @PostMapping(path = "/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto saveRequest(@Positive @PathVariable long id,
                                               @RequestParam long eventId) {
        log.info("POST /users/{}/requests?eventid={}", id, eventId);
        return requestService.saveRequest(id, eventId);
    }

    @PatchMapping(path = "/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@Positive @PathVariable long id,
                                                 @Positive @PathVariable long requestId) {
        log.info("PATCH /users/{}/request/{}/cancel", id, requestId);
        return requestService.cancelRequest(id, requestId);
    }

    @GetMapping(path = "/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestById(@Positive @PathVariable long id,
                                                        @Positive @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}/requests", id, eventId);
        return requestService.getRequestById(id, eventId);
    }

    @PatchMapping(path = "/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestById(@Positive @PathVariable long id,
                                                            @Positive @PathVariable long eventId,
                                                            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("PATCH /users/{}/events/{}/requests", id, eventId);
        return requestService.changeRequestById(id, eventId, eventRequestStatusUpdateRequest);
    }
}
