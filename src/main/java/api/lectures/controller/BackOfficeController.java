package api.lectures.controller;


import api.lectures.controller.dto.CreateLectureDto;
import api.lectures.entities.Lecture;
import api.lectures.services.LectureService;
import api.lectures.services.dto.AttenderDto;
import api.lectures.services.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/back")
@RequiredArgsConstructor
public class BackOfficeController {

    private final LectureService lectureService;

    @GetMapping("/lecture/{lectureId}/details")
    public Mono<ResponseEntity<LectureDto>> getLectureDetails(@PathVariable Long lectureId) {
        return lectureService.getLectureDetails(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // 여러 강의 조회
    @GetMapping("/lecture/details")
    public Mono<ResponseEntity<List<LectureDto>>> getAllLectureDetails() {
        return lectureService.getLectureAll()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @PostMapping("/lecture")
    public Mono<ResponseEntity<Lecture>> createLecture(@RequestBody CreateLectureDto request) {
        return lectureService.createLecture(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @GetMapping("/lecture/{lectureId}/attenders")
    public Flux<ResponseEntity<AttenderDto>> getAttenders(@PathVariable Long lectureId) {
        return lectureService.getAttendersByLectureId(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
