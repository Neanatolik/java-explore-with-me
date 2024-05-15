package ru.practicum.main_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.event.*;
import ru.practicum.main_service.dto.mapper.CommentMapperMapStruct;
import ru.practicum.main_service.dto.mapper.EventMapperMapStruct;
import ru.practicum.main_service.enums.EventCommentState;
import ru.practicum.main_service.enums.EventState;
import ru.practicum.main_service.enums.RequestResponseState;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.repository.*;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.MainService.DATE_FORMAT;
import static ru.practicum.main_service.enums.EventState.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final EventMapperMapStruct eventMapperMapStruct;
    private final CommentMapperMapStruct commentMapperMapStruct;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Event> events = eventRepository.getByEventsByParametersAdmin(Objects.nonNull(users) ? users : Collections.emptyList(),
                Objects.nonNull(states) ? states : Collections.emptyList(),
                Objects.nonNull(categories) ? categories : Collections.emptyList(),
                rangeStart, rangeEnd, page);
        List<String> url = events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList());
        Map<Long, Integer> views = getMapViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), url);
        return eventMapperMapStruct.mapToEventFullDto(events, views, getMapComments(events));
    }

    @Override
    public EventFullDto changeEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        if (Objects.nonNull(updateEventAdminRequest.getLocation())) {
            locationRepository.save(updateEventAdminRequest.getLocation());
        }
        Event event = checkUpdateEventAdminRequestAndGetEvent(updateEventAdminRequest, id);
        return eventMapperMapStruct.toEventFullDto(event, getViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), id), getListComments(id));
    }

    @Override
    public EventFullDto saveEvent(long id, NewEventDto newEventDto) {
        locationRepository.save(newEventDto.getLocation());
        checkNewEventDto(newEventDto);
        Integer views = getViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), id);
        Event event = eventRepository.save(eventMapperMapStruct.fromNewEventDto(newEventDto,
                categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException("category не найдена", "Ошибка запроса")),
                userRepository.findById(id).orElseThrow(() -> new NotFoundException("user не найден", "Ошибка запроса")),
                LocalDateTime.now(), PENDING, 0L));
        return eventMapperMapStruct.toEventFullDto(event, views, getListComments(event.getId()));
    }

    @Override
    public EventFullDto changeEventById(long id, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найден", "Ошбка запроса"));
        checkUpdateEventUserRequest(updateEventUserRequest, event.getState());
        if (Objects.nonNull(updateEventUserRequest.getLocation())) {
            locationRepository.save(updateEventUserRequest.getLocation());
        }
        Integer views = getViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), id);
        Event eventNew = updateEventData(updateEventUserRequest, event);
        return eventMapperMapStruct.toEventFullDto(eventNew, views, getListComments(event.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventById(long id, long eventId) {
        Integer views = getViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), eventId);
        return eventMapperMapStruct.toEventFullDto(eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найдено", "Ошибка запроса")), views, getListComments(eventId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByParameters(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            checkDates(rangeStart, rangeEnd);
        }
        List<Event> events = eventRepository.getByEventsByParameters(text, Objects.nonNull(categories) ? categories : Collections.emptyList(), paid, rangeStart, rangeEnd, page);
        List<String> url = events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList());
        Map<Long, Integer> views = getMapViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), url);
        return eventMapperMapStruct.mapToEventShortDto(events, views);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUserId(long id, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Event> events = eventRepository.getByUserId(id, page);
        List<String> url = events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList());
        Map<Long, Integer> views = getMapViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), url);
        return eventMapperMapStruct.mapToEventShortDto(events, views);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventByIdPublic(long id) {
        Event event = getEventById(id);
        Integer views = getViews(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(2), id);
        return eventMapperMapStruct.toEventFullDto(event, views, getListComments(id));
    }

    private Map<Long, List<CommentDto>> getMapComments(List<Event> events) {
        Map<Long, List<CommentDto>> comments = new HashMap<>();
        for (Event event : events) {
            comments.put(event.getId(), commentMapperMapStruct.mapToCommentDto(commentRepository.findByEventId(event.getId())));
        }
        return comments;
    }

    private List<CommentDto> getListComments(long eventId) {
        return commentMapperMapStruct.mapToCommentDto(commentRepository.findByEventId(eventId));
    }

    private Integer getViews(LocalDateTime start, LocalDateTime end, long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<Object> re = statsClient.getHits(List.of("/events/" + id),
                start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                end.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                true);
        List<ViewStatsDto> responseList = objectMapper.convertValue(re.getBody(), new TypeReference<>() {
        });
        if (responseList.isEmpty()) return 0;
        return responseList.get(0).getHits();
    }

    private Map<Long, Integer> getMapViews(LocalDateTime start, LocalDateTime end, List<String> requestList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseEntity<Object> re = statsClient.getHits(requestList,
                start.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                end.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                true);
        List<ViewStatsDto> responseList = objectMapper.convertValue(re.getBody(), new TypeReference<>() {
        });
        Map<String, Integer> mapViewsWithString = new HashMap<>();
        for (ViewStatsDto viewStatsDto : responseList) {
            mapViewsWithString.put(viewStatsDto.getUri(), viewStatsDto.getHits());
        }
        Map<Long, Integer> mapViews = new HashMap<>();
        for (String s : requestList) {
            mapViews.put(Long.valueOf(s.replace("/events/", "")), Objects.nonNull(mapViewsWithString.get(s)) ? mapViewsWithString.get(s) : 0);
        }
        return mapViews;
    }

    private void checkUpdateEventUserRequest(UpdateEventUserRequest updateEventUserRequest, EventState state) {
        if (state.equals(PUBLISHED)) {
            throw new Conflict("Событие уже опубликовано", "Ошибка запроса");
        }
        if (Objects.nonNull(updateEventUserRequest.getEventDate())) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("event date раньше чем через 2 часа", "Ошибка запроса");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getEventDate())) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("start позже чем end", "Ошибка запроса");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getDescription())) {
            if (updateEventUserRequest.getDescription().isBlank()) {
                throw new BadRequest("нет Description", "Неверный запрос");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getAnnotation())) {
            if (updateEventUserRequest.getAnnotation().isBlank()) {
                throw new BadRequest("нет Annotation", "Неверный запрос");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getTitle())) {
            if (updateEventUserRequest.getTitle().isBlank()) {
                throw new BadRequest("title отсутствует", "Ошибка запроса");
            }
        }
    }

    private Event getEventById(long eventId) {
        return eventRepository.findPublishedById(eventId).orElseThrow(() -> new NotFoundException("event published не найден", "Ошибка запроса"));
    }

    private Event checkUpdateEventAdminRequestAndGetEvent(UpdateEventAdminRequest u, long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event не найден", "Ошибка запроса"));
        if (Objects.nonNull(u.getLocation())) {
            event.setLocation(u.getLocation());
        }
        if (Objects.nonNull(u.getCommentState())) {
            event.setCommentState(u.getCommentState());
        }
        if (Objects.nonNull(u.getCategory())) {
            event.setCategory(categoryRepository.getReferenceById(Long.valueOf(u.getCategory())));
        }
        if (Objects.nonNull(u.getEventDate())) {
            if (LocalDateTime.parse(u.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("Дата начала события ранее чем через два часа с текущего момента", "Ошибка обновления данных");
            }
            event.setEventDate(LocalDateTime.parse(u.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)));
        }
        if (Objects.nonNull(u.getDescription())) {
            if (u.getDescription().isBlank()) {
                throw new BadRequest("нет Description", "Неверный запрос");
            }
            event.setDescription(u.getDescription());
        }
        if (Objects.nonNull(u.getAnnotation())) {
            if (u.getAnnotation().isBlank()) {
                throw new BadRequest("нет Annotation", "Неверный запрос");
            }
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getTitle())) {
            if (u.getTitle().isBlank()) {
                throw new BadRequest("Отсуствует Title", "Ошибка запроса");
            }
            event.setTitle(u.getTitle());
        }
        if (Objects.nonNull(u.getPaid())) {
            event.setPaid(u.getPaid());
        }
        if (Objects.nonNull(u.getParticipantLimit())) {
            event.setParticipantLimit(u.getParticipantLimit());
        }
        if (Objects.nonNull(u.getRequestModeration())) {
            event.setRequestModeration(u.getRequestModeration());
        }
        if (Objects.nonNull(u.getStateAction())) {
            switch (u.getStateAction()) {
                case PUBLISH_EVENT:
                    if (event.getState().equals(PUBLISHED)) {
                        throw new Conflict("Повторный запрос", "Ошибка запроса");
                    }
                    if (event.getState().equals(CANCELED)) {
                        throw new Conflict("Уже оплубликован", "Ошибка запроса");
                    }
                    event.setState(PUBLISHED);
                    break;
                case REJECT_EVENT:
                    if (event.getState().equals(CANCELED)) {
                        throw new Conflict("Повторный запрос", "Ошибка запроса");
                    }
                    if (event.getState().equals(PUBLISHED)) {
                        throw new Conflict("Уже отменен", "Ошибка запроса");
                    }
                    event.setState(CANCELED);
                    event.setCommentState(EventCommentState.CLOSE);
                    break;
            }
        }
        return event;
    }

    private void checkDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd.isBefore(rangeStart)) {
            throw new BadRequest("Дата start позже end", "Ошибка запроса");
        }
    }

    private Event updateEventData(UpdateEventUserRequest u, Event event) {
        if (Objects.nonNull(u.getAnnotation())) {
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getCommentState())) {
            event.setCommentState(u.getCommentState());
        }
        if (Objects.nonNull(u.getCategory())) {
            event.setCategory(categoryRepository.findById(Long.valueOf(u.getCategory())).orElseThrow(() -> new NotFoundException("Данная category не найдена", "Ошибка запроса")));
        }
        if (Objects.nonNull(u.getDescription())) {
            event.setDescription(u.getDescription());
        }
        if (Objects.nonNull(u.getEventDate())) {
            event.setEventDate(LocalDateTime.parse(u.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)));
        }
        if (Objects.nonNull(u.getLocation())) {
            event.setLocation(u.getLocation());
        }
        if (Objects.nonNull(u.getPaid())) {
            event.setPaid(u.getPaid());
        }
        if (Objects.nonNull(u.getParticipantLimit())) {
            event.setParticipantLimit(u.getParticipantLimit());
        }
        if (Objects.nonNull(u.getRequestModeration())) {
            event.setRequestModeration(u.getRequestModeration());
        }
        if (Objects.nonNull(u.getStateAction())) {
            switch (RequestResponseState.valueOf(u.getStateAction())) {
                case CANCEL_REVIEW:
                    event.setState(CANCELED);
                    event.setCommentState(EventCommentState.CLOSE);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(PENDING);
                    break;
            }
        }
        if (Objects.nonNull(u.getTitle())) {
            event.setTitle(u.getTitle());
        }
        return event;
    }

    private void checkNewEventDto(NewEventDto newEventDto) {
        if (LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern(DATE_FORMAT)).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequest("event date раньше чем через 2 часа", "Ошибка запроса");
        }
    }
}
