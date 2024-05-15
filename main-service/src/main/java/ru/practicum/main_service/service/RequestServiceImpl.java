package ru.practicum.main_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.mapper.RequestMapperMapStruct;
import ru.practicum.main_service.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.main_service.dto.request.ParticipationRequestDto;
import ru.practicum.main_service.enums.EventState;
import ru.practicum.main_service.enums.RequestState;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.Request;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.RequestRepository;
import ru.practicum.main_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.practicum.main_service.enums.RequestState.*;

@Service
@Transactional
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapperMapStruct requestMapperMapStruct;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestById(long id, long eventId) {
        return requestMapperMapStruct.mapToParticipationRequestDto(requestRepository.getReferenceByIds(eventId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequests(long id) {
        return requestMapperMapStruct.mapToParticipationRequestDto(requestRepository.findByUserId(id));
    }

    @Override
    public ParticipationRequestDto saveRequest(long id, long eventId) {
        checkExistingRequest(id, eventId);
        Event event = getEventById(eventId);
        User user = getUserById(id);
        checkRequester(id, eventId);
        checkEventState(event.getState());
        checkParticipationLimit(event.getParticipantLimit(), requestRepository.getConfirmedRequestForEvent(eventId));
        Request request = newRequest(event, user);
        if (!event.getRequestModeration()) {
            request.setStatus(CONFIRMED);
        }
        Request requestFromDb = requestRepository.save(request);
        eventRepository.updateEvent(requestRepository.getConfirmedRequestForEvent(eventId), eventId);
        return requestMapperMapStruct.toParticipationRequestDto(requestFromDb);
    }

    @Override
    public ParticipationRequestDto cancelRequest(long id, long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Данный request не найден", "Ошибка запроса"));
        request.setStatus(CANCELED);
        return requestMapperMapStruct.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestById(long id, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateResult e = new EventRequestStatusUpdateResult();
        Event event = getEventById(eventId);
        List<Request> listOfRequests = new ArrayList<>();
        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {
            Request request = getRequestByIdFromDb(requestId);
            if (request.getStatus().equals(CONFIRMED) && eventRequestStatusUpdateRequest.getStatus().equals(REJECTED)) {
                throw new Conflict("Попытка отменить подтвержденный request", "Ошибка запроса");
            }
            request.setStatus(eventRequestStatusUpdateRequest.getStatus());
            listOfRequests.add(request);
        }
        int size = listOfRequests.size();
        switch (eventRequestStatusUpdateRequest.getStatus()) {
            case CONFIRMED:
                if ((event.getConfirmedRequests() + size) > event.getParticipantLimit()) {
                    throw new Conflict("Превышен ParticipantLimit", "Ошибка подтверждения запросов");
                }
                e.setConfirmedRequests(requestMapperMapStruct.mapToParticipationRequestDto(listOfRequests));
                break;
            case REJECTED:
                e.setRejectedRequests(requestMapperMapStruct.mapToParticipationRequestDto(listOfRequests));
                size = 0;
                break;
        }
        eventRepository.incrementConfirmedRequests(eventId, size);
        return e;
    }

    private Request newRequest(Event event, User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestState.CONFIRMED);
        } else {
            request.setStatus(RequestState.PENDING);
        }
        return request;
    }

    private void checkParticipationLimit(int participantLimit, int requests) {
        if (participantLimit != 0) {
            if (requests == participantLimit) {
                throw new Conflict("Превышен лимит участников", "Ошибка создания request");
            }
        }
    }

    private void checkEventState(EventState state) {
        if (state.equals(EventState.PENDING)) {
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

    private Request getRequestByIdFromDb(Long requestId) {
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new NotFoundException("request не найден", "Ошибка запроса");
        } else return request.get();
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найден", "Ошибка запроса"));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user не найден", "Ошибка запроса"));
    }
}
