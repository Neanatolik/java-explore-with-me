package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.event.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto changeEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto saveEvent(long id, NewEventDto newEventDto);

    EventFullDto changeEventById(long id, long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto getEventById(long id, long eventId);

    List<EventShortDto> getEventsByParameters(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size);

    List<EventShortDto> getEventsByUserId(long id, int from, int size);

    EventFullDto getEventByIdPublic(long id);

}
