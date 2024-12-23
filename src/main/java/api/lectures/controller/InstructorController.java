package api.lectures.controller;


import api.lectures.entities.Instructor;
import api.lectures.services.dto.InstructorDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @PostMapping("")
    public Mono<InstructorDto> registerInstructor(@RequestBody InstructorDto instructorDto) {
        return Mono.just(instructorDto);
    }
}
