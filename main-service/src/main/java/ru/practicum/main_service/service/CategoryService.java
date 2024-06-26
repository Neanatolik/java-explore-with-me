package ru.practicum.main_service.service;

import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long id);

    CategoryDto changeCategory(long id, CategoryDto categoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoriesById(long id);
}
