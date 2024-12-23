package api.lectures.controller;


import api.lectures.controller.dto.CreateInstructorDto;
import api.lectures.entities.Instructor;
import api.lectures.services.InstructorService;
import api.lectures.services.dto.InstructorDto;
import api.lectures.services.dto.VenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping("")
    public Mono<ResponseEntity<Instructor>> registerInstructor(@RequestBody CreateInstructorDto createInstructorDto) {
        return instructorService.create(createInstructorDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<InstructorDto>> getInstructorById(@PathVariable("id") Long id) {
        return instructorService.findInstructorById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @GetMapping("")
    public Mono<ResponseEntity<List<InstructorDto>>> getAllInstructors() {
        return instructorService.findAllInstructors()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
