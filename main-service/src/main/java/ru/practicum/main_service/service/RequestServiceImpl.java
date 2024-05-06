package ru.practicum.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.dto.EventRequestStatusUpdateResult;
import ru.practicum.main_service.dto.ParticipationRequestDto;
import ru.practicum.main_service.dto.mapper.RequestMapper;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.enums.StateState;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.Request;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.RequestRepository;
import ru.practicum.main_service.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;


    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ParticipationRequestDto> getRequestById(long id, long eventId) {
        return RequestMapper.mapToParticipationRequestDto(requestRepository.getReferenceByIds(eventId));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(long id) {
        return RequestMapper.mapToParticipationRequestDto(requestRepository.findByUserId(id));
    }

    @Override
    public ParticipationRequestDto saveRequest(long id, long eventId) {
        checkExistingRequest(id, eventId);
        Event event = getEventById(eventId);
        User user = getUserById(id);
        checkRequester(id, eventId);
        checkEventState(event.getState());
        checkParticipationLimit(event.getParticipantLimit(), requestRepository.getConfirmedRequestForEvent(eventId));
        Request request = RequestMapper.newRequest(event, user);
        if (!event.getRequestModeration()) {
            request.setStatus("CONFIRMED");
        }
        Request requestFromDb = requestRepository.save(request);
        eventRepository.updateEvent(requestRepository.getConfirmedRequestForEvent(eventId), eventId);
        return RequestMapper.toParticipationRequestDto(requestFromDb);
    }

    private void checkParticipationLimit(int participantLimit, int requests) {
        System.out.println("participantLimit: " + participantLimit + " requests: " + requests);
        if (participantLimit!=0) {
            if (requests == participantLimit) {
                throw new Conflict("", "Ошибка создания request");
            }
        }
    }

    private void checkEventState(String state) {
        if (state.equals("PENDING")) {
            throw new Conflict("Event ещё не опубликован", "Ошибка создания request");
        }
    }

    private void checkRequester(long id, long eventId) {
        if (eventRepository.checkRequesterForRequest(eventId, id)) {
            throw new Conflict("Запрос владельцом пользователя события", "Ошибка создания request");
        }
    }

    private void checkExistingRequest(long userId, long eventId) {
        if (requestRepository.existsRequest(userId, eventId)) {
            throw new Conflict("У данного пользователя уже есть запрос к данному событию", "Ошибка создания request");
        }
    }

    @Override
    public ParticipationRequestDto cancelRequest(long id, long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new NotFoundException("", "");
        }
        request.get().setStatus(State.CANCELED.toString());
        return RequestMapper.toParticipationRequestDto(requestRepository.save(request.get()));
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestById(long id, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateResult e = new EventRequestStatusUpdateResult();
        Event event = getEventById(eventId);
        List<ParticipationRequestDto> listOfRequests = new ArrayList<>();
        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {
            Request request = getRequestByIdFromDb(requestId);
            if (request.getStatus().equals("CONFIRMED") & eventRequestStatusUpdateRequest.getStatus().equals("REJECTED")) {
                throw new Conflict("", "");
            }
            request.setStatus(eventRequestStatusUpdateRequest.getStatus());
            listOfRequests.add(RequestMapper.toParticipationRequestDto(request));
            requestRepository.save(request);
        }
        int size = listOfRequests.size();
        switch (StateState.valueOf(eventRequestStatusUpdateRequest.getStatus())) {
            case CONFIRMED:
                if ((event.getConfirmedRequests() + size) > event.getParticipantLimit()) {
                    throw new Conflict("", "");
                }
                e.setConfirmedRequests(listOfRequests);
                break;
            case REJECTED:
                e.setRejectedRequests(listOfRequests);
                size = 0;
                break;
        }
        eventRepository.incrementConfirmedRequests(eventId, size);
        return e;
    }

    private Request getRequestByIdFromDb(Long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new NotFoundException("", "");
        } else return request.get();
    }

    private Event getEventById(long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("", "");
        } else return event.get();
    }

    private Event getPublishedEventById(long eventId) {
        Optional<Event> event = eventRepository.findPublishedById(eventId);
        if (event.isEmpty()) {
            System.out.println("Здесь event");
            throw new NotFoundException("", "");
        } else return event.get();
    }

    private User getUserById(long userId) {
        System.out.println("Здесь user");
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("", "");
        } else return user.get();
    }
}
