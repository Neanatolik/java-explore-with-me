package ru.practicum.main_service.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.dto.NewCategoryDto;
import ru.practicum.main_service.model.Category;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CategoryMapper {
    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category fromCategoryDto(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        return category;
    }

    public static Category fromNewCategoryDto(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static List<CategoryDto> mapToCategoryDto(List<Category> categories) {
        List<CategoryDto> dtoList = new ArrayList<>();
        for (Category category : categories) {
            dtoList.add(toCategoryDto(category));
        }
        return dtoList;
    }
}
