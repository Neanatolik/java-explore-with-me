package ru.practicum.main_service.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.comment.NewCommentDto;
import ru.practicum.main_service.dto.comment.UpdateCommentDto;
import ru.practicum.main_service.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentsController {

    private final CommentService commentService;

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOwnComment(@PathVariable long userId, @PathVariable long eventId, @PathVariable long commentId) {
        log.info("DELETE /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        commentService.deleteOwnComment(userId, eventId, commentId);
    }

    @GetMapping(path = "/{commentId}")
    public CommentDto getComment(@PathVariable long userId, @PathVariable long eventId, @PathVariable long commentId) {
        log.info("GET /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        return commentService.getComment(userId, eventId, commentId);
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable long userId, @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}/comments", userId, eventId);
        return commentService.getComments(userId, eventId);
    }

    @PatchMapping(path = "/{commentId}")
    public CommentDto changeComment(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @PathVariable long commentId,
                                    @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("PATCH /users/{}/events/{}/comments/{}", userId, eventId, commentId);
        return commentService.changeComment(userId, eventId, commentId, updateCommentDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable long userId,
                                  @PathVariable long eventId,
                                  @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /users/{}/events/{}/comments", userId, eventId);
        return commentService.postComment(userId, eventId, newCommentDto);
    }

}
