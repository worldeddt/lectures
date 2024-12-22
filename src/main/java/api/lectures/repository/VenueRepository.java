package api.lectures.repository;


import api.lectures.entities.Venue;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends ReactiveCrudRepository<Venue, Long> {
}
