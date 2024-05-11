package ru.practicum.main_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select *\n" +
            "from users\n" +
            "where id in(:ids)", nativeQuery = true)
    List<User> findAllById(List<Long> ids, PageRequest page);

    @Query(value = "select *\n" +
            "from users", nativeQuery = true)
    List<User> findAll(PageRequest page);

}
