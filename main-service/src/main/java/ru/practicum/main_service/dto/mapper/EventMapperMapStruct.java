package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.EventShortDto;
import ru.practicum.main_service.dto.event.NewEventDto;
import ru.practicum.main_service.enums.EventState;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.MainService.DATE_FORMAT;


@Mapper(componentModel = "spring")
public interface EventMapperMapStruct {
    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = DATE_FORMAT)
    @Mapping(target = "views", source = "views")
    @Mapping(target = "comments", source = "comments")
    EventFullDto toEventFullDto(Event event, Integer views, List<CommentDto> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "eventDate", source = "newEventDto.eventDate", dateFormat = DATE_FORMAT)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "state", source = "eventState")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    Event fromNewEventDto(NewEventDto newEventDto, Category category, User user, LocalDateTime createdOn, EventState eventState, Long confirmedRequests);

    @Mapping(target = "category", source = "event.category")
    @Mapping(target = "eventDate", source = "event.eventDate", dateFormat = DATE_FORMAT)
    @Mapping(target = "initiator", source = "event.initiator")
    @Mapping(target = "views", source = "views")
    EventShortDto toEventShortDto(Event event, Integer views);

    default List<EventShortDto> mapToEventShortDto(Iterable<Event> events, Map<Long, Integer> views) {
        List<EventShortDto> dtoList = new ArrayList<>();
        for (Event event : events) {
            dtoList.add(toEventShortDto(event, views.get(event.getId())));
        }
        return dtoList;
    }

    default List<EventFullDto> mapToEventFullDto(Iterable<Event> events, Map<Long, Integer> views, Map<Long, List<CommentDto>> comments) {
        List<EventFullDto> dtoList = new ArrayList<>();
        for (Event event : events) {
            dtoList.add(toEventFullDto(event, views.get(event.getId()), comments.get(event.getId())));
        }
        return dtoList;
    }

    default Set<EventShortDto> mapToEventShortDtoSet(Set<Event> events, Map<Long, Integer> views) {
        Set<EventShortDto> dtoList = new HashSet<>();
        for (Event event : events) {
            dtoList.add(toEventShortDto(event, Objects.nonNull(views) ? views.get(event.getId()) : 0));
        }
        return dtoList;
    }

}
