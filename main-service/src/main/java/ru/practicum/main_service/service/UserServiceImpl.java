package ru.practicum.main_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.NewUserRequest;
import ru.practicum.main_service.dto.UserDto;
import ru.practicum.main_service.dto.mapper.UserMapper;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.repository.*;
import ru.practicum.stats.StatsClient;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, EventRepository eventRepository, CompilationRepository compilationRepository, LocationRepository locationRepository, RequestRepository requestRepository, StatsClient statsClient, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto saveUser(NewUserRequest newUserRequest) {
        checkUser(newUserRequest);
        checkEmail(newUserRequest.getEmail());
        try {
            return UserMapper.toUserDto(userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
        } catch (Exception e) {
            throw new Conflict("Введите другую почту", "Пользователь с данной почтой уже существует");
        }
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        if (Objects.nonNull(ids)) {
            return UserMapper.mapToUserDto(userRepository.findAllById(ids, page));
        } else return UserMapper.mapToUserDto(userRepository.findAll(page));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    private void checkUser(NewUserRequest newUserRequest) {
        if (Objects.isNull(newUserRequest.getName()) || newUserRequest.getName().isBlank()) {
            throw new BadRequest("Укажите имя", "Пустое значение имени");
        }
        if(newUserRequest.getName().length() < 2) {
            throw new BadRequest("Длина имени должна быть 2 и более символа", "Неверное значение имени");
        }
        if(newUserRequest.getName().length() > 250) {
            throw new BadRequest("Длина имени должна быть меньше 250 символов", "Неверное значение имени");
        }
    }

    private void checkEmail(String email) {
        if(Objects.isNull(email)) {
            throw new BadRequest("", "");
        }
        if(email.length()==254) return;
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new BadRequest("Укажите верное значение почты", "Неверное значение почты");
        }
    }

}
