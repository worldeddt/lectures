package api.lectures.repository;

import api.lectures.entities.Lecture;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LectureRepository extends ReactiveCrudRepository<Lecture, Long> {
}
