package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main_service.dto.user.NewUserRequest;
import ru.practicum.main_service.dto.user.UserDto;
import ru.practicum.main_service.dto.user.UserShortDto;
import ru.practicum.main_service.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperMapStruct {
    UserDto toUserDto(User user);

    UserShortDto toUserShortDto(User user);

    User fromNewUserRequest(NewUserRequest newUserRequest);

    List<UserDto> mapToUserDto(List<User> users);
}
