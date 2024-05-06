package ru.practicum.main_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.User;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select *\n" +
            "from categories", nativeQuery = true)
    List<Category> findAll(PageRequest page);

    @Query(value = "SELECT EXISTS(select name\n" +
            "from categories\n" +
            "where lower(name) = lower(:category))", nativeQuery = true)
    boolean existName(String category);
}
