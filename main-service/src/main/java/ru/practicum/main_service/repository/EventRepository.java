package ru.practicum.main_service.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select *\n" +
            "from events e \n" +
            "where e.user_id = :id", nativeQuery = true)
    List<Event> getByUserId(long id, PageRequest page);

    @Query(value = "select *\n" +
            "from events e \n" +
            "where (:text is null or lower(e.annotation) like lower(concat('%',:text,'%')) or lower(e.description) like lower(concat('%',:text,'%')))\n" +
            "and (:categories is null or e.category_id in (:categories))\n" +
            "and (:paid is null or e.paid = :paid)\n" +
            "and (CAST(:rangeStart AS timestamp) is null or e.event_date > CAST(:rangeStart AS timestamp))\n" +
            "and (CAST(:rangeEnd AS timestamp) is null or e.event_date < CAST(:rangeEnd AS timestamp))", nativeQuery = true)
    List<Event> getByEventsByParameters(String text,
                                        List<Integer> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        PageRequest page);

    @Query(value = "select *\n" +
            "from events e \n" +
            "where e.id = :id and e.state = 'PUBLISHED'", nativeQuery = true)
    Optional<Event> findPublishedById(long id);

    @Query(value = "select *\n" +
            "from events e \n" +
            "where (:users is null or e.user_id in (:users))\n" +
            "and (:states is null or e.state in (:states))\n" +
            "and (:categories is null or e.category_id in (:categories))\n" +
            "and (CAST(:rangeStart AS timestamp) is null or e.event_date > CAST(:rangeStart AS timestamp))\n" +
            "and (CAST(:rangeEnd AS timestamp) is null or e.event_date < CAST(:rangeEnd AS timestamp))", nativeQuery = true)
    Iterable<Event> getByEventsByParametersAdmin(List<Integer> users,
                                                 List<String> states,
                                                 List<Integer> categories,
                                                 LocalDateTime rangeStart,
                                                 LocalDateTime rangeEnd,
                                                 PageRequest page);

    @Modifying(clearAutomatically = true)
    @Query(value = "update events e\n" +
            "set confirmed_requests = confirmed_requests + :size\n" +
            "where e.id = :id", nativeQuery = true)
    void incrementConfirmedRequests(long id, int size);

    @Modifying(clearAutomatically = true)
    @Query(value = "update events e\n" +
            "set views = :hits\n" +
            "where e.id = :id", nativeQuery = true)
    void setView(long id, Integer hits);

    @Query(value = "SELECT EXISTS(select category_id\n" +
            "from events\n" +
            "where category_id = :id)", nativeQuery = true)
    boolean existsCategory(long id);

    @Query(value = "SELECT EXISTS(select id\n" +
            "from events\n" +
            "where id = :eventId and user_id = :id)", nativeQuery = true)
    boolean checkRequesterForRequest(long eventId, long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update events e\n" +
            "set confirmed_requests = :confirmedRequests\n" +
            "where e.id = :id", nativeQuery = true)
    void updateEvent(long confirmedRequests, long id);
}
