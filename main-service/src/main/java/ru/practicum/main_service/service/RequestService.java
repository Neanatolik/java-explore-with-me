package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.main_service.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.main_service.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getRequestById(long id, long eventId);

    List<ParticipationRequestDto> getRequests(long id);

    ParticipationRequestDto saveRequest(long id, long eventId);

    ParticipationRequestDto cancelRequest(long id, long requestId);

    EventRequestStatusUpdateResult changeRequestById(long id, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
