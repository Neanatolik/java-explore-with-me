package ru.practicum.main_service.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.service.CommentService;

@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
@Slf4j
public class AdminCommentsController {

    private final CommentService commentService;

    @DeleteMapping(path = "/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable long commentId) {
        log.info("DELETE /admin/comments/{}", commentId);
        commentService.deleteComment(commentId);
    }

    @GetMapping(path = "/{commentId}")
    public CommentDto getComment(@PathVariable long commentId) {
        log.info("GET /admin/comments/{}", commentId);
        return commentService.getCommentForAdmin(commentId);
    }

}
