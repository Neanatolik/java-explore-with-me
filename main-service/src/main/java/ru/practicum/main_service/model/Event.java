package ru.practicum.main_service.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main_service.enums.EventCommentState;
import ru.practicum.main_service.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.MainService.DATE_FORMAT;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @DateTimeFormat(pattern = DATE_FORMAT)
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @DateTimeFormat(pattern = DATE_FORMAT)
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User initiator;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "location")
    private Location location;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    @Column(name = "comment_state")
    @Enumerated(EnumType.STRING)
    private EventCommentState commentState;
}
