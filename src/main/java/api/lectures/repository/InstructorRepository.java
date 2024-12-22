package api.lectures.repository;


import api.lectures.entities.Instructor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends ReactiveCrudRepository<Instructor, Long> {
}
