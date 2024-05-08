package ru.practicum.main_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.dto.NewCategoryDto;
import ru.practicum.main_service.dto.mapper.CategoryMapper;
import ru.practicum.main_service.exceptions.Conflict;
import ru.practicum.main_service.exceptions.NotFoundException;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.repository.CategoryRepository;
import ru.practicum.main_service.repository.EventRepository;

import java.util.List;

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
        checkCategory(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    public void deleteCategory(long id) {
        checkRelatedEvent(id);
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Данная категория не найдена", "Ошибка запроса"));
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto changeCategory(long id, CategoryDto categoryDto) {
        Category oldCategory = getCategoryById(id);
        if (!categoryDto.getName().equals(oldCategory.getName())) {
            checkCategory(categoryDto.getName());
        }
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
        return CategoryMapper.toCategoryDto(getCategoryById(id));
    }

    private Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Данная категория не найдена", "Ошибка запроса"));
    }

    private void checkRelatedEvent(long id) {
        if (eventRepository.existsCategory(id)) {
            throw new Conflict("Данная category используется", "Ошибка удаления");
        }
    }

    private void checkCategory(String name) {
        if (categoryRepository.existName(name)) {
            throw new Conflict("Данное имя категории уже существует", "Ошибка получения данных");
        }
    }

}
