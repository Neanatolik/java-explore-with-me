package ru.practicum.main_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "select *\n" +
            "from compilations c\n" +
            "where :pinned is null or c.pinned = :pinned", nativeQuery = true)
    List<Compilation> getCompilations(boolean pinned, PageRequest page);
}
