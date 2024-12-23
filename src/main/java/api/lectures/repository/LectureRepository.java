package api.lectures.repository;

import api.lectures.entities.Lecture;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface LectureRepository extends ReactiveCrudRepository<Lecture, Long> {

    @Query("SELECT * FROM lecture WHERE NOW() BETWEEN start_time AND DATE_ADD(start_time, INTERVAL 1 DAY)")
    Flux<Lecture> findAllAvailableLectures();
}
