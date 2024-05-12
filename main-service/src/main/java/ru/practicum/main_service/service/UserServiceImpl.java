package ru.practicum.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.mapper.UserMapper;
import ru.practicum.main_service.dto.user.NewUserRequest;
import ru.practicum.main_service.dto.user.UserDto;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(NewUserRequest newUserRequest) {
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
        } catch (Exception e) {
            throw new Conflict("Введите другую почту", "Пользователь с данной почтой уже существует");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (Objects.nonNull(ids)) {
            return UserMapper.mapToUserDto(userRepository.findAllById(ids, page));
        } else return UserMapper.mapToUserDto(userRepository.findAll(page));
    }

    @Override
    public void deleteUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Данный user не найден", "Ошибка запроса");
        }
    }

}
