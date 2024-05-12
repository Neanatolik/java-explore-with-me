package ru.practicum.main_service.dto.event;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.main_service.enums.EventCommentState;
import ru.practicum.main_service.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @NonNull
    private String eventDate;
    private Location location;
    @Builder.Default
    private Boolean paid = false;
    @PositiveOrZero
    @Builder.Default
    private Integer participantLimit = 0;
    @Builder.Default
    private Boolean requestModeration = true;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
    @Builder.Default
    private EventCommentState commentState = EventCommentState.OPEN;
}
