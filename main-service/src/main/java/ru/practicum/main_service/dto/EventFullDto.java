package ru.practicum.main_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.enums.EventState;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;
}
