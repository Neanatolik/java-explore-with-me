package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main_service.dto.comment.CommentDto;
import ru.practicum.main_service.dto.comment.NewCommentDto;
import ru.practicum.main_service.model.Comment;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapperMapStruct {
    @Mapping(target = "event", source = "comment.event.id")
    @Mapping(target = "commentator", source = "comment.commentator.id")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> mapToCommentDto(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "commentState", ignore = true)
    @Mapping(target = "edited", ignore = true)
    @Mapping(target = "created", source = "created")
    @Mapping(target = "event", source = "event")
    @Mapping(target = "commentator", source = "user")
    Comment fromNewCommentDto(NewCommentDto newCommentDto, Event event, User user, LocalDateTime created);

}
