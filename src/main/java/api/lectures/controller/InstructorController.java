package api.lectures.controller;


import api.lectures.services.dto.InstructorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {
    private final InstructorService instructorService;

    @PostMapping("")
    public Mono<InstructorDto> registerInstructor(@RequestBody InstructorDto instructorDto) {
        return Mono.just(instructorDto);
    }
}
