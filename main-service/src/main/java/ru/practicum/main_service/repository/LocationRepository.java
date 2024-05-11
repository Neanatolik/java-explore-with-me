package ru.practicum.main_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {


}
