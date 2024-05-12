package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.comment.NewCommentDto;
import ru.practicum.main_service.dto.comment.UpdateCommentDto;

import java.util.List;

public interface CommentService {

    void deleteComment(long id);

    void deleteOwnComment(long userId, long eventId, long commentId);

    CommentDto postComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentDto getComment(long userId, long eventId, long commentId);

    CommentDto getCommentForAdmin(long commentId);

    List<CommentDto> getComments(long userId, long eventId);

    CommentDto changeComment(long userId, long eventId, long commentId, UpdateCommentDto updateCommentDto);
}
