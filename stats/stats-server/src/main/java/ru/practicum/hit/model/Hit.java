package ru.practicum.hit.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
