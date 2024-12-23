package api.lectures.repository;


import api.lectures.entities.LectureApplication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureApplicationRepository extends ReactiveCrudRepository<LectureApplication, String> {
}
