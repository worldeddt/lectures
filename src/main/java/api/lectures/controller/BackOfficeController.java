package api.lectures.controller;


import api.lectures.entities.Lecture;
import api.lectures.services.LectureService;
import api.lectures.services.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/back")
@RequiredArgsConstructor
public class BackOfficeController {

    private final LectureService lectureService;

    @GetMapping("/lectuers")
    public Flux<List<LectureDto>>> findAllLectures() {
        return ResponseEntity.ok().body(
                lectureService.findAllLectures()
        );
    }

    @GetMapping("/{lectureId}/details")
    public Mono<ResponseEntity<LectureDto>> getLectureDetails(@PathVariable Long lectureId) {
        return lectureService.getLectureDetails(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
