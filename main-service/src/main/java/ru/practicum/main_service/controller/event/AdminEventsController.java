package ru.practicum.main_service.controller.event;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.UpdateEventAdminRequest;
import ru.practicum.main_service.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/admin/events")
@Validated
@Slf4j
public class AdminEventsController {
    private final EventService eventService;

    @Autowired
    public AdminEventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                Objects.nonNull(users) ? StringUtils.join("&users=", users) : "null",
                Objects.nonNull(states) ? StringUtils.join("&states=", states) : "null",
                Objects.nonNull(categories) ? StringUtils.join("&categories=", categories) : "null",
                Objects.nonNull(rangeStart) ? rangeStart : "null",
                Objects.nonNull(rangeEnd) ? rangeEnd : "null",
                from,
                size
        );
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/{id}")
    public EventFullDto changeEvent(@PathVariable Long id,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH /admin/events/{}", id);
        return eventService.changeEvent(id, updateEventAdminRequest);
    }

}
