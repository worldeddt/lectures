package api.lectures.controller;


import api.lectures.entities.Lecture;
import api.lectures.services.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/backOffice")
@RequiredArgsConstructor
public class BackOfficeController {

    private final LectureService lectureService;

    @GetMapping("/lectuers")
    public Flux<Lecture> findAllLectures() {
        return
    }
}
