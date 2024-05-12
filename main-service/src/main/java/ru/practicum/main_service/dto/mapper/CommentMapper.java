package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.comment.NewCommentDto;
import ru.practicum.main_service.dto.comment.UpdateCommentDto;
import ru.practicum.main_service.model.Comment;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getCreated(),
                comment.getEdited(),
                comment.getEvent().getId(),
                comment.getCommentator().getId()
        );
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> comments) {
        List<CommentDto> dtoList = new ArrayList<>();
        for (Comment comment : comments) {
            dtoList.add(toCommentDto(comment));
        }
        return dtoList;
    }

    public static Comment fromNewCommentDto(NewCommentDto newCommentDto, Event event, User user) {
        Comment comment = new Comment();
        comment.setComment(newCommentDto.getComment());
        comment.setEvent(event);
        comment.setCommentator(user);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static Comment fromUpdateCommentDto(UpdateCommentDto updateCommentDto, Event event, User user) {
        Comment comment = new Comment();
        comment.setComment(updateCommentDto.getComment());
        comment.setEvent(event);
        comment.setCommentator(user);
        return comment;
    }
}
