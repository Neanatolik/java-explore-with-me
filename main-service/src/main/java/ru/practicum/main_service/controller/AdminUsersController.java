package ru.practicum.main_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.NewUserRequest;
import ru.practicum.main_service.dto.UserDto;
import ru.practicum.main_service.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/admin/users")
@Validated
@Slf4j
public class AdminUsersController {
    private final UserService userService;

    public AdminUsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("POST /admin/users");
        return userService.saveUser(newUserRequest);
    }

    @GetMapping()
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                  @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users?ids={}&from={}&size={}",
                Objects.nonNull(ids) ? StringUtils.join("&ids=", ids) : "null",
                from,
                size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        log.info("DELETE /admin/users/{}", id);
        userService.deleteUser(id);
    }
}
