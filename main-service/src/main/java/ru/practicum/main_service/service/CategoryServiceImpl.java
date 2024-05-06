package ru.practicum.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.dto.NewCategoryDto;
import ru.practicum.main_service.dto.mapper.CategoryMapper;
import ru.practicum.main_service.exceptions.BadRequest;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.repository.CategoryRepository;
import ru.practicum.main_service.repository.EventRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        checkCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public void deleteCategory(long id) {
        checkRelatedEvent(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto changeCategory(long id, CategoryDto categoryDto) {
        Category oldCategory = categoryRepository.getReferenceById(id);
        checkCategoryDto(categoryDto, oldCategory.getName());
        oldCategory.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(oldCategory));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return CategoryMapper.mapToCategoryDto(categoryRepository.findAll(page));
    }

    @Override
    public CategoryDto getCategoriesById(long id) {
        return getCategoryById(id);
    }

    private CategoryDto getCategoryById(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException("category не найдена", "Ошибка запроса");
        }
        return CategoryMapper.toCategoryDto(category.get());
    }

    private void checkCategoryDto(CategoryDto categoryDto, String name) {
        if (Objects.isNull(categoryDto) || categoryDto.getName().isBlank()) {
            throw new BadRequest("У category отсутствует name", "Ошибка запроса");
        }
        if (categoryDto.getName().length() > 50) {
            throw new BadRequest("name должен быть меньше 50 символов", "Ошибка запроса");
        }
        if (categoryRepository.existName(categoryDto.getName()) && !categoryDto.getName().equals(name)) {
            throw new Conflict("Данное name же существует", "Ошибка запроса");
        }
    }

    private void checkRelatedEvent(long id) {
        if (eventRepository.existsCategory(id)) {
            throw new Conflict("Данная category используется", "Ошибка удаления");
        }
    }

    private void checkCategory(NewCategoryDto newCategoryDto) {
        if (Objects.isNull(newCategoryDto.getName()) || newCategoryDto.getName().isBlank()) {
            throw new BadRequest("Отсутствует name", "Ошибка получения данных");
        }
        if (newCategoryDto.getName().length() > 50) {
            throw new BadRequest("name должен быть менее 50 символов", "Ошибка получения данных");
        }
        if (categoryRepository.existName(newCategoryDto.getName())) {
            throw new Conflict("Данное имя категории уже существует", "Ошибка получения данных");
        }
    }

}
