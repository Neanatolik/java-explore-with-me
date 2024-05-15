package ru.practicum.main_service.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.category.CategoryDto;
import ru.practicum.main_service.dto.category.NewCategoryDto;
import ru.practicum.main_service.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
public class AdminCategoriesController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories");
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long id) {
        log.info("DELETE /admin/categories/{}", id);
        categoryService.deleteCategory(id);
    }

    @PatchMapping(path = "/{id}")
    public CategoryDto changeCategory(@PathVariable long id,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("PATCH /admin/categories/{}", id);
        return categoryService.changeCategory(id, categoryDto);
    }
}
