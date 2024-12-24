package api.lectures.controller;


import api.lectures.controller.dto.CreateVenueDto;
import api.lectures.entities.Venue;
import api.lectures.services.VenueService;
import api.lectures.services.dto.VenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venue")
public class VenueController {
    private final VenueService venueService;

    @PostMapping("")
    public Mono<ResponseEntity<Venue>> create(@RequestBody CreateVenueDto createVenueDto) {
        return venueService.create(createVenueDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<VenueDto>> getAttenderById(@PathVariable("id") Long id) {
        return venueService.findVenueById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("")
    public Mono<ResponseEntity<List<VenueDto>>> getAllVenues() {
        return venueService.findAllVenues()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
