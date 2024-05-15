package ru.practicum.main_service.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.category.CategoryDto;
import ru.practicum.main_service.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PublicCategoriesController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                           @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GET /categories");
        return categoryService.getCategories(from, size);
    }

    @GetMapping(path = "/{id}")
    public CategoryDto getCategoriesById(@PathVariable long id) {
        log.info("GET /categories/{}", id);
        return categoryService.getCategoriesById(id);
    }
}
