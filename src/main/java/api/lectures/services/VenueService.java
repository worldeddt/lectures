package api.lectures.services;


import api.lectures.controller.dto.CreateVenueDto;
import api.lectures.entities.Venue;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.VenueRepository;
import api.lectures.services.dto.VenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Venue> create(CreateVenueDto createVenueDto) {
        return venueRepository.save(
                Venue.builder()
                        .address(createVenueDto.getAddress())
                        .name(createVenueDto.getName())
                        .seatCount(createVenueDto.getSeatCount())
                        .build()
        );
    }

    public Mono<VenueDto> findVenueById(Long id) {
        return venueRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(ErrorCode.NOT_FOUND_VENUE.build())
                )
                .flatMap(venue -> Mono.just(
                        VenueDto.builder()
                                .id(venue.getId())
                                .name(venue.getName())
                                .address(venue.getAddress())
                                .seatCount(venue.getSeatCount())
                                .build()
                        )
                );
    }

    public Mono<List<VenueDto>> findAllVenues() {
        return venueRepository.findAll()
                .collectList()
                .flatMap(
                        venues -> {
                            if (venues.isEmpty()) {
                                return Mono.empty();
                            } else {
                                List<VenueDto> venueDtos = new ArrayList<>();

                                for (Venue venue : venues) {
                                    venueDtos.add(
                                            VenueDto.builder()
                                                    .id(venue.getId())
                                                    .name(venue.getName())
                                                    .address(venue.getAddress())
                                                    .seatCount(venue.getSeatCount())
                                                    .build()
                                    );
                                }

                                return Mono.just(venueDtos);
                            }
                        }
                );
    }
}
