package ru.practicum.main_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "select *\n" +
            "from requests\n" +
            "where user_id = :id", nativeQuery = true)
    List<Request> findByUserId(long id);

    @Query(value = "select *\n" +
            "from requests\n" +
            "where event_id = :eventId", nativeQuery = true)
    List<Request> getReferenceByIds(long eventId);

    @Query(value = "SELECT EXISTS(select id\n" +
            "from requests\n" +
            "where event_id = :eventId and user_id = :userId)", nativeQuery = true)
    boolean existsRequest(long userId, long eventId);

    @Query(value = "select count(*)\n" +
            "from requests\n" +
            "where event_id = :eventId and status = 'CONFIRMED'", nativeQuery = true)
    int getConfirmedRequestForEvent(long eventId);
}
