package ru.practicum.main_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.comment.NewCommentDto;
import ru.practicum.main_service.dto.comment.UpdateCommentDto;
import ru.practicum.main_service.dto.mapper.CommentMapperMapStruct;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Comment;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.repository.CommentRepository;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapperMapStruct commentMapperMapStruct;

    @Override
    public void deleteComment(long id) {
        commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment не найден", "Ошибка запроса"));
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteOwnComment(long userId, long eventId, long commentId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найден", "Ошибка запроса"));
        checkOwningComment(eventId, userId, commentId);
        checkCommentState(eventId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto postComment(long userId, long eventId, NewCommentDto newCommentDto) {
        Event event = getEventById(eventId);
        checkCommentState(eventId);
        Comment comment = commentMapperMapStruct.fromNewCommentDto(newCommentDto, event, getUserById(userId), LocalDateTime.now());
        return commentMapperMapStruct.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getComment(long userId, long eventId, long commentId) {
        checkUser(userId);
        checkEvent(eventId);
        checkCommentState(eventId);
        return commentMapperMapStruct.toCommentDto(commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment не найден", "Ошибка запроса")));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentForAdmin(long commentId) {
        return commentMapperMapStruct.toCommentDto(commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment не найден", "Ошибка запроса")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(long userId, long eventId) {
        checkUser(userId);
        checkEvent(eventId);
        checkCommentState(eventId);
        return commentMapperMapStruct.mapToCommentDto(commentRepository.findByEventId(eventId));
    }

    @Override
    public CommentDto changeComment(long userId, long eventId, long commentId, UpdateCommentDto updateCommentDto) {
        checkUser(userId);
        checkEvent(eventId);
        checkCommentState(eventId);
        Comment comment = checkAndUpdateComment(userId, commentId, updateCommentDto);
        return commentMapperMapStruct.toCommentDto(comment);
    }

    private void checkCommentState(long eventId) {
        if (eventRepository.checkCommentState(eventId)) {
            throw new Conflict("Event закрыт для comment", "Ошибка запроса");
        }
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user не найден", "Ошибка запроса"));
    }

    private Event getEventById(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найден", "Ошибка запроса"));
    }

    private void checkOwningComment(long eventId, long userId, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("comment не найден", "Ошибка запроса"));
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new BadRequest("comment принадлежит другому user", "Ошибка запроса");
        }
    }

    private void checkUser(long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user не найден", "Ошибка запроса"));

    }

    private void checkEvent(long eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("event не найден", "Ошибка запроса"));
    }

    private Comment checkAndUpdateComment(long userId, long commentId, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("comment не найден", "Ошибка запроса"));
        if (!comment.getCommentator().getId().equals(userId)) {
            throw new BadRequest("comment принадлежит другому user", "Ошибка запроса");
        }
        comment.setComment(updateCommentDto.getComment());
        comment.setEdited(LocalDateTime.now());
        return comment;
    }
}
