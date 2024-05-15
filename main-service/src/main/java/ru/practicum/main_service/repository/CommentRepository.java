package ru.practicum.main_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(long eventId);
}
