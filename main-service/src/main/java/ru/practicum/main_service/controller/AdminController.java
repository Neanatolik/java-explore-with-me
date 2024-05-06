package ru.practicum.main_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.*;
import ru.practicum.main_service.service.CategoryService;
import ru.practicum.main_service.service.CompilationService;
import ru.practicum.main_service.service.EventService;
import ru.practicum.main_service.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminController {
    private final UserService userService;
    private final EventService eventService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;

    @Autowired
    public AdminController(UserService userService, EventService eventService, CategoryService categoryService, CompilationService compilationService) {
        this.userService = userService;
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.compilationService = compilationService;
    }

    @PostMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("POST /admin/users");
        return userService.saveUser(newUserRequest);
    }

    @GetMapping(path = "/users")
    public List<UserDto> getUser(@RequestParam(required = false) List<Long> ids,
                                 @RequestParam(defaultValue = "0") int from,
                                 @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users?ids={}&from={}&size={}",
                Objects.nonNull(ids) ? StringUtils.join("&ids=", ids) : "null",
                from,
                size);
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping(path = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long id) {
        log.info("DELETE /admin/users/{}", id);
        userService.deleteUser(id);
    }

    @PostMapping(path = "/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories");
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long id) {
        log.info("DELETE /admin/categories/{}", id);
        categoryService.deleteCategory(id);
    }

    @PatchMapping(path = "/categories/{id}")
    public CategoryDto changeCategory(@PathVariable long id, @RequestBody CategoryDto categoryDto) {
        log.info("PATCH /admin/categories/{}", id);
        return categoryService.changeCategory(id, categoryDto);
    }

    @GetMapping(path = "/events")
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                Objects.nonNull(users) ? StringUtils.join("&users=", users) : "null",
                Objects.nonNull(states) ? StringUtils.join("&states=", states) : "null",
                Objects.nonNull(categories) ? StringUtils.join("&categories=", categories) : "null",
                Objects.nonNull(rangeStart) ? rangeStart : "null",
                Objects.nonNull(rangeEnd) ? rangeEnd : "null",
                from,
                size
        );
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path = "/events/{id}")
    public EventFullDto getEvents(@PathVariable Long id,
                                  @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("PATCH /admin/events/{}", id);
        return eventService.changeEvent(id, updateEventAdminRequest);
    }

    @PostMapping(path = "/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations");
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping(path = "/compilations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long id) {
        log.info("DELETE /compilations/{}", id);
        compilationService.deleteCompilation(id);
    }

    @PatchMapping(path = "/compilations/{id}")
    public CompilationDto changeCompilation(@RequestBody UpdateCompilationRequest updateCompilationRequest,
                                            @PathVariable long id) {
        log.info("PATCH /compilations/{}", id);
        return compilationService.changeCompilation(updateCompilationRequest, id);
    }

}
