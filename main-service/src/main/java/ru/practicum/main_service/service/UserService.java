package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.NewUserRequest;
import ru.practicum.main_service.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto saveUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(long id);
}
