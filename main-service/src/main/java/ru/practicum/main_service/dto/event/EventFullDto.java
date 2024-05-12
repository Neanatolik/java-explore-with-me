package ru.practicum.main_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.user.UserShortDto;
import ru.practicum.main_service.enums.EventCommentState;
import ru.practicum.main_service.enums.EventState;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Location;

import java.time.LocalDateTime;
import java.util.List;

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
    private EventCommentState commentState;
    private List<CommentDto> comments;
}
