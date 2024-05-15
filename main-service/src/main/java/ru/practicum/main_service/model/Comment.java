package ru.practicum.main_service.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main_service.enums.CommentState;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.MainService.DATE_FORMAT;

@Getter
@Setter
@Entity
@Table(name = "comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @DateTimeFormat(pattern = DATE_FORMAT)
    private LocalDateTime created;
    @DateTimeFormat(pattern = DATE_FORMAT)
    private LocalDateTime edited;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User commentator;
    @Column(name = "comment_state")
    private CommentState commentState;
}
