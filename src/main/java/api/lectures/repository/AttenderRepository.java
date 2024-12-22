package api.lectures.repository;

import api.lectures.entities.Attender;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AttenderRepository extends ReactiveCrudRepository<Attender, Long> {
}
