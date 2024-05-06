package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.EventFullDto;
import ru.practicum.main_service.dto.EventShortDto;
import ru.practicum.main_service.dto.NewEventDto;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class EventMapper {

    public EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event fromNewEventDto(NewEventDto newEventDto, Category category, User user) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setInitiator(user);
        event.setState(State.PENDING.toString());
        event.setViews(0);
        return event;
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtoList = new ArrayList<>();
        for (Event event : events) {
            dtoList.add(toEventShortDto(event));
        }
        return dtoList;
    }

    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtoList = new ArrayList<>();
        for (Event event : events) {
            dtoList.add(toEventFullDto(event));
        }
        return dtoList;
    }

    public static Set<EventShortDto> mapToEventShortDtoSet(Set<Event> events) {
        Set<EventShortDto> dtoList = new HashSet<>();
        for (Event event : events) {
            System.out.println("Event: " + event);
            dtoList.add(toEventShortDto(event));
        }
        return dtoList;
    }
}
