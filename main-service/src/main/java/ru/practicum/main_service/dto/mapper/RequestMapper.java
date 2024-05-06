package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.ParticipationRequestDto;
import ru.practicum.main_service.dto.UserDto;
import ru.practicum.main_service.enums.State;
import ru.practicum.main_service.enums.StateState;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.Request;
import ru.practicum.main_service.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getId(),
                request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public Request newRequest(Event event, User user) {
        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(user);
        if(event.getParticipantLimit()==0) {
            request.setStatus(StateState.CONFIRMED.toString());
        } else {
            request.setStatus(State.PENDING.toString());
        }
        return request;
    }

    public Request fromParticipationRequestDto(ParticipationRequestDto participationRequestDto, Event event, User user) {
        Request request = new Request();
        request.setId(participationRequestDto.getId());
        request.setCreated(LocalDateTime.parse(participationRequestDto.getCreated()));
        request.setEvent(event);
        request.setRequester(user);
        request.setStatus(participationRequestDto.getStatus());
        return request;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(List<Request> requests) {
        List<ParticipationRequestDto> dtoList = new ArrayList<>();
        for (Request request : requests) {
            dtoList.add(toParticipationRequestDto(request));
        }
        return dtoList;
    }
}
