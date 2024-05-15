package ru.practicum.main_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.dto.category.CategoryDto;
import ru.practicum.main_service.dto.user.UserShortDto;
import ru.practicum.main_service.enums.EventState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private EventState state;
    private String title;
    private Integer views;
}
