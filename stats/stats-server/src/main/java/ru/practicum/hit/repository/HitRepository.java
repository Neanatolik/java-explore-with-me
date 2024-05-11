package ru.practicum.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDtoIn;
import ru.practicum.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "select app, uri, count(distinct ip) as hits\n" +
            "from hits as h\n" +
            "where h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countUniqueWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(value = "select app, uri, count(distinct ip) as hits\n" +
            "from hits as h\n" +
            "where uri IN (:uris)\n" +
            "and (h.timestamp > CAST(:start AS timestamp) or CAST(:start AS timestamp) is null)\n" +
            "and (h.timestamp < CAST(:end AS timestamp) or CAST(:end AS timestamp) is null)\n" +
            "group by app, uri, ip\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countUniqueByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select app, uri, count(id) as hits\n" +
            "from hits as h\n" +
            "where uri IN (:uris) and h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countNonUniqueByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select app, uri, count(id) as hits\n" +
            "from hits as h\n" +
            "where h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countNonUniqueWithoutUris(LocalDateTime start, LocalDateTime end);
}
