package ru.practicum.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatsDtoIn;
import ru.practicum.hit.model.Hit;

import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "select app, uri, count(id) as hits\n" +
            "from hits as h\n" +
            "where uri IN (:uris) and h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> count(String start, String end, List<String> uris);

    @Query(value = "select app, uri, count(id) as hits\n" +
            "from hits as h\n" +
            "where h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countFromStartToEndByNotUnique(String start, String end);

    @Query(value = "select app, uri, count(distinct ip) as hits\n" +
            "from hits as h\n" +
            "where uri IN (:uris) and h.timestamp between :start and :end\n" +
            "group by app, uri, ip\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countUnique(@Param("start") String start, @Param("end") String end, @Param("uris") List<String> uris);

    @Query(value = "select app, uri, count(distinct id) as hits\n" +
            "from hits as h\n" +
            "where uri IN (:uris) and h.timestamp between :start and :end\n" +
            "group by app, uri\n" +
            "order by hits desc", nativeQuery = true)
    List<ViewStatsDtoIn> countUnique2(String start, String end, List<String> uris);
}
