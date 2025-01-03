package api.lectures.repository;

import api.lectures.entities.Attender;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttenderRepository extends ReactiveCrudRepository<Attender, Long> {
}
