package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.EventFullDto;
import ru.practicum.main_service.dto.NewUserRequest;
import ru.practicum.main_service.dto.UserDto;
import ru.practicum.main_service.dto.UserShortDto;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public User fromUserDto(UserDto userDto) {
        User newUser = new User();
        newUser.setId(userDto.getId());
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        return newUser;
    }

    public static User fromNewUserRequest(NewUserRequest newUserRequest) {
        User newUser = new User();
        newUser.setEmail(newUserRequest.getEmail());
        newUser.setName(newUserRequest.getName());
        return newUser;
    }

    public static List<UserDto> mapToUserDto(List<User> users) {
        List<UserDto> dtoList = new ArrayList<>();
        for (User user : users) {
            dtoList.add(toUserDto(user));
        }
        return dtoList;
    }
}
