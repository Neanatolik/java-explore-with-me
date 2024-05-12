package ru.practicum.main_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select *\n" +
            "from comments c \n" +
            "where c.event_id = :eventId", nativeQuery = true)
    List<Comment> findByEventId(long eventId);
}
