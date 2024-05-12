package ru.practicum.main_service.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;
    private LocalDateTime created;
    private LocalDateTime edited;
    private Long event;
    private Long commentator;
}
