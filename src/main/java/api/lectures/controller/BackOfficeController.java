package api.lectures.controller;


import api.lectures.controller.dto.CreateLectureDto;
import api.lectures.entities.Lecture;
import api.lectures.services.LectureService;
import api.lectures.services.dto.AttenderDto;
import api.lectures.services.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
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

    @Description("특정 강연 상세")
    @GetMapping("/lecture/{lectureId}/details")
    public Mono<ResponseEntity<LectureDto>> getLectureDetails(@PathVariable Long lectureId) {
        return lectureService.getLectureDetails(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Description("강연 목록")
    @GetMapping("/lecture/details")
    public Mono<ResponseEntity<List<LectureDto>>> getAllLectureDetails() {
        return lectureService.getLectureAll()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @Description("강연 등록")
    @PostMapping("/lecture")
    public Mono<ResponseEntity<Lecture>> createLecture(@RequestBody CreateLectureDto request) {
        return lectureService.createLecture(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    @Description("강연 신청자 목록")
    @GetMapping("/lecture/{lectureId}/attenders")
    public Mono<ResponseEntity<List<AttenderDto>>> getAttenders(@PathVariable Long lectureId) {
        return lectureService.getAttendersByLectureId(lectureId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
