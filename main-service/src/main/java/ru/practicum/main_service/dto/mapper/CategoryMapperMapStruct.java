package ru.practicum.main_service.dto.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main_service.dto.category.CategoryDto;
import ru.practicum.main_service.dto.category.NewCategoryDto;
import ru.practicum.main_service.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapperMapStruct {

    CategoryDto toCategoryDto(Category category);

    Category fromNewCategoryDto(NewCategoryDto newCategoryDto);

    List<CategoryDto> mapToCategoryDto(List<Category> categories);

}
