package ru.practicum.main_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.main_service.dto.*;
import ru.practicum.main_service.dto.mapper.EventMapper;
import ru.practicum.main_service.enums.StateActionAdmin;
import ru.practicum.main_service.enums.StateActionUser;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.repository.CategoryRepository;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.LocationRepository;
import ru.practicum.main_service.repository.UserRepository;
import ru.practicum.stats.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.practicum.main_service.enums.State.*;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final UserRepository userRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository, CategoryRepository categoryRepository, StatsClient statsClient, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.categoryRepository = categoryRepository;
        this.statsClient = statsClient;
        this.userRepository = userRepository;
    }


    @Override
    public List<EventFullDto> getEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return EventMapper.mapToEventFullDto(eventRepository.getByEventsByParametersAdmin(Objects.nonNull(users) ? users : Collections.emptyList(),
                Objects.nonNull(states) ? states : Collections.emptyList(),
                Objects.nonNull(categories) ? categories : Collections.emptyList(),
                rangeStart, rangeEnd, page));
    }

    @Override
    public EventFullDto changeEvent(Long id, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkUpdateEventAdminRequestAndGetEvent(updateEventAdminRequest, id);
        if (Objects.nonNull(updateEventAdminRequest.getLocation())) {
            locationRepository.save(updateEventAdminRequest.getLocation());
        }
        return EventMapper.toEventFullDto(eventRepository.save(updateEventDataAdmin(updateEventAdminRequest, event)));
    }

    private Event checkUpdateEventAdminRequestAndGetEvent(UpdateEventAdminRequest updateEventAdminRequest, long id) {
        if (Objects.nonNull(updateEventAdminRequest.getEventDate())) {
            if (LocalDateTime.parse(updateEventAdminRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("", "");
            }
        }
        if (Objects.nonNull(updateEventAdminRequest.getDescription())) {
            if (updateEventAdminRequest.getDescription().isBlank()) {
                throw new BadRequest("нет Description", "Неверный запрос");
            }
            if (updateEventAdminRequest.getDescription().length() < 20 || updateEventAdminRequest.getDescription().length() > 7000) {
                throw new BadRequest("", "Неверный запрос");
            }
        }

        if (Objects.nonNull(updateEventAdminRequest.getAnnotation())) {
            if (updateEventAdminRequest.getAnnotation().isBlank()) {
                throw new BadRequest("нет Annotation", "Неверный запрос");
            }
            if (updateEventAdminRequest.getAnnotation().length() < 20 || updateEventAdminRequest.getAnnotation().length() > 2000) {
                throw new BadRequest("", "Неверный запрос");
            }
        }

        if (Objects.nonNull(updateEventAdminRequest.getTitle())) {
            if (updateEventAdminRequest.getTitle().isBlank()) {
                throw new BadRequest("", "");
            }
            if (updateEventAdminRequest.getTitle().length() < 3 || updateEventAdminRequest.getTitle().length() > 120) {
                throw new BadRequest("", "");
            }
        }

        if (Objects.nonNull(updateEventAdminRequest.getParticipantLimit())) {
            if (updateEventAdminRequest.getParticipantLimit() < 0) {
                throw new BadRequest("Отрицательное ParticipantLimit", "Неверный запрос");
            }
        }
        Optional<Event> event = eventRepository.findById(id);
        if (event.isEmpty()) {
            throw new NotFoundException("", "");
        }
        System.out.println("event: " + event.get().getState() + " update: " + updateEventAdminRequest.getStateAction());
        System.out.println(Objects.nonNull(updateEventAdminRequest.getStateAction()));
        if (Objects.nonNull(updateEventAdminRequest.getStateAction())) {
            switch (updateEventAdminRequest.getStateAction()) {
                case "PUBLISH_EVENT":
                    if (event.get().getState().equals("PUBLISHED")) {
                        throw new Conflict("", "");
                    }
                    if (event.get().getState().equals("CANCELED")) {
                        throw new Conflict("", "");
                    }
                    break;
                case "REJECT_EVENT":
                    if (event.get().getState().equals("CANCELED")) {
                        throw new Conflict("", "");
                    }
                    if (event.get().getState().equals("PUBLISHED")) {
                        throw new Conflict("", "");
                    }
                    break;
            }
        }
        if (event.get().getState().equals(updateEventAdminRequest.getStateAction())) {
            throw new Conflict("", "");
        }
        return event.get();
    }

    @Override
    public EventFullDto saveEvent(long id, NewEventDto newEventDto) {
        locationRepository.save(newEventDto.getLocation());
        checkNewEventDto(newEventDto);
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.fromNewEventDto(newEventDto, categoryRepository.getReferenceById(newEventDto.getCategory()), userRepository.getReferenceById(id))));
    }

    @Override
    public EventFullDto changeEventById(long id, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.getReferenceById(eventId);
        checkUpdateEventUserRequest(updateEventUserRequest, event.getState());
        if (Objects.nonNull(updateEventUserRequest.getLocation())) {
            locationRepository.save(updateEventUserRequest.getLocation());
        }
        return EventMapper.toEventFullDto(eventRepository.save(updateEventDataAdmin(updateEventUserRequest, event)));
    }

    private void checkUpdateEventUserRequest(UpdateEventUserRequest updateEventUserRequest, String state) {
        if (state.equals("PUBLISHED")) {
            throw new Conflict("", "");
        }
        if (Objects.nonNull(updateEventUserRequest.getEventDate())) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("", "");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getEventDate())) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequest("", "");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getDescription())) {
            if (updateEventUserRequest.getDescription().isBlank()) {
                throw new BadRequest("нет Description", "Неверный запрос");
            }
            if (updateEventUserRequest.getDescription().length() < 20 || updateEventUserRequest.getDescription().length() > 7000) {
                throw new BadRequest("", "Неверный запрос");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getAnnotation())) {
            if (updateEventUserRequest.getAnnotation().isBlank()) {
                throw new BadRequest("нет Annotation", "Неверный запрос");
            }
            if (updateEventUserRequest.getAnnotation().length() < 20 || updateEventUserRequest.getAnnotation().length() > 2000) {
                throw new BadRequest("", "Неверный запрос");
            }
        }
        if (Objects.nonNull(updateEventUserRequest.getTitle())) {
            if (updateEventUserRequest.getTitle().isBlank()) {
                throw new BadRequest("", "");
            }
            if (updateEventUserRequest.getTitle().length() < 3 || updateEventUserRequest.getTitle().length() > 120) {
                throw new BadRequest("", "");
            }
        }
    }

    @Override
    public EventFullDto getEventById(long id, long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("", "");
        }
        return EventMapper.toEventFullDto(event.get());
    }

    @Override
    public List<EventShortDto> getEventsByParameters(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (Objects.nonNull(categories)) {
            checkCategories(categories);
        }
        if (Objects.nonNull(rangeStart) && Objects.nonNull(rangeEnd)) {
            checkDates(rangeStart, rangeEnd);
        }
        return EventMapper.mapToEventShortDto(eventRepository.getByEventsByParameters(text, Objects.nonNull(categories) ? categories : Collections.emptyList(), paid, rangeStart, rangeEnd, page));
    }

    @Override
    public List<EventShortDto> getEventsByUserId(long id, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return EventMapper.mapToEventShortDto(eventRepository.getByUserId(id, page));
    }

    @Override
    public EventFullDto getEventByIdPublic(long id) {
        setView(id, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        return EventMapper.toEventFullDto(getEventById(id));
    }

    @Transactional
    private void setView(long id, LocalDateTime start, LocalDateTime end) {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = "/events/" + id;
        ResponseEntity<Object> re = statsClient.getHits(List.of(s), start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), true);
        List<ViewStatsDto> list = objectMapper.convertValue(re.getBody(), new TypeReference<>() {
        });
        System.out.println(list);
        eventRepository.setView(id, list.get(0).getHits());
    }

    private Event getEventById(long eventId) {
        Optional<Event> event = eventRepository.findPublishedById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("", "");
        } else return event.get();
    }

    private User getUserById(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("", "");
        } else return user.get();
    }

    private void checkDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd.isBefore(rangeStart)) {
            throw new BadRequest("", "");
        }
    }

    private void checkCategories(List<Integer> categories) {
        for (Integer category : categories) {
            if (category <= 0) {
                throw new BadRequest("", "");
            }
        }
    }

    private void checkNewEventDto(NewEventDto newEventDto) {
        if (Objects.isNull(newEventDto.getDescription()) || newEventDto.getDescription().isBlank()) {
            throw new BadRequest("нет Description", "Неверный запрос");
        }
        if (newEventDto.getDescription().length() < 20 || newEventDto.getDescription().length() > 7000) {
            throw new BadRequest("", "Неверный запрос");

        }
        if (Objects.isNull(newEventDto.getAnnotation()) || newEventDto.getAnnotation().isBlank()) {
            throw new BadRequest("нет Annotation", "Неверный запрос");
        }
        if (newEventDto.getAnnotation().length() < 20 || newEventDto.getAnnotation().length() > 2000) {
            throw new BadRequest("", "Неверный запрос");
        }

        if (Objects.isNull(newEventDto.getTitle()) || newEventDto.getTitle().isBlank()) {
            throw new BadRequest("", "");
        }
        if (newEventDto.getTitle().length() < 3 || newEventDto.getTitle().length() > 120) {
            throw new BadRequest("", "");
        }

        if (Objects.nonNull(newEventDto.getParticipantLimit())) {
            if (newEventDto.getParticipantLimit() < 0) {
                throw new BadRequest("Отрицательное ParticipantLimit", "Неверный запрос");
            }
        }
        if (Objects.isNull(newEventDto.getEventDate())) {
            throw new BadRequest("", "");
        }

        if (LocalDateTime.parse(newEventDto.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequest("", "");
        }

    }

    private Event updateEventDataAdmin(UpdateEventUserRequest u, Event event) {
        if (Objects.nonNull(u.getAnnotation())) {
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getCategory())) {
            event.setCategory(categoryRepository.getReferenceById(Long.valueOf(u.getCategory())));
        }
        if (Objects.nonNull(u.getDescription())) {
            event.setDescription(u.getDescription());
        }
        if (Objects.nonNull(u.getEventDate())) {
            event.setEventDate(LocalDateTime.parse(u.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (Objects.nonNull(u.getLocation())) {
            event.setLocation(u.getLocation());
        }
        if (Objects.nonNull(u.getAnnotation())) {
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getPaid())) {
            event.setPaid(u.getPaid());
        }
        if (Objects.nonNull(u.getParticipantLimit())) {
            if (u.getParticipantLimit() < 0) {
                throw new BadRequest("Отрицательное ParticipantLimit", "Неверный запрос");
            }
            event.setParticipantLimit(u.getParticipantLimit());
        }
        if (Objects.nonNull(u.getRequestModeration())) {
            event.setRequestModeration(u.getRequestModeration());
        }
        if (Objects.nonNull(u.getStateAction())) {
            switch (StateActionUser.valueOf(u.getStateAction())) {
                case CANCEL_REVIEW:
                    event.setState(CANCELED.toString());
                    break;
                case SEND_TO_REVIEW:
                    event.setState(PENDING.toString());
                    break;
                default:
                    throw new BadRequest("", "");
            }
        }
        if (Objects.nonNull(u.getTitle())) {
            event.setTitle(u.getTitle());
        }
        return event;
    }

    private Event updateEventDataAdmin(UpdateEventAdminRequest u, Event event) {
        if (Objects.nonNull(u.getAnnotation())) {
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getCategory())) {
            event.setCategory(categoryRepository.getReferenceById(Long.valueOf(u.getCategory())));
        }
        if (Objects.nonNull(u.getDescription())) {
            event.setDescription(u.getDescription());
        }
        if (Objects.nonNull(u.getEventDate())) {
            event.setEventDate(LocalDateTime.parse(u.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (Objects.nonNull(u.getLocation())) {
            event.setLocation(u.getLocation());
        }
        if (Objects.nonNull(u.getAnnotation())) {
            event.setAnnotation(u.getAnnotation());
        }
        if (Objects.nonNull(u.getPaid())) {
            event.setPaid(u.getPaid());
        }
        if (Objects.nonNull(u.getParticipantLimit())) {
            if (u.getParticipantLimit() < 0) {
                throw new BadRequest("Отрицательное ParticipantLimit", "Неверный запрос");
            }
            event.setParticipantLimit(u.getParticipantLimit());
        }
        if (Objects.nonNull(u.getRequestModeration())) {
            event.setRequestModeration(u.getRequestModeration());
        }
        if (Objects.nonNull(u.getStateAction())) {
            switch (StateActionAdmin.valueOf(u.getStateAction())) {
                case REJECT_EVENT:
                    event.setState(CANCELED.toString());
                    break;
                case PUBLISH_EVENT:
                    event.setState(PUBLISHED.toString());
                    break;
                default:
                    throw new BadRequest("", "");
            }
        }
        if (Objects.nonNull(u.getTitle())) {
            event.setTitle(u.getTitle());
        }
        return event;
    }
}
