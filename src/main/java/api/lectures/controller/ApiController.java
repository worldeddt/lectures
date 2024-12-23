package api.lectures.controller;


import api.lectures.controller.dto.ResponseLectureApplicationDto;
import api.lectures.controller.dto.ResponseLectureDto;
import api.lectures.entities.Lecture;
import api.lectures.exception.ErrorCode;
import api.lectures.services.LectureApplicationService;
import api.lectures.services.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/front")
@RequiredArgsConstructor
public class ApiController {

    private final LectureService lectureService;
    private final LectureApplicationService lectureApplicationService;

    @Description("신청한 강연 취소(사번 입력)")
    @GetMapping("/applications/{attenderNumber}")
    public Flux<ResponseLectureApplicationDto> getApplicationsByAttenderNumber(
            @PathVariable String attenderNumber) {
        return lectureApplicationService.getApplicationsByAttenderNumber(attenderNumber)
                .switchIfEmpty(
                        Mono.error(ErrorCode.NOT_FOUND_LECTURE.build())
                ).flatMap(lectureApplication ->
                        Flux.just(
                        ResponseLectureApplicationDto.builder()
                                .id(lectureApplication.getId())
                                .lectureId(lectureApplication.getLectureId())
                                .status(lectureApplication.getStatus())
                                .attenderId(lectureApplication.getAttenderId())
                                .build()
                        )
                );
    }

    @Description("강연신청(사번 입력,같은 강연 중복 신청 제한)")
    @PostMapping("/lecture/{lectureId}/apply")
    public Mono<ResponseEntity<Object>> applyForLecture(@PathVariable Long lectureId, @RequestParam Long attenderId) {
        return lectureApplicationService.applyForLecture(lectureId, attenderId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @Description("강연 목록(신청 가능한 시점 부터 강연 시작 시간 1일 후까지 노출)")
    @GetMapping("/lecture/available")
    public Flux<ResponseLectureDto> getAvailableLectures() {
        return lectureService.getAvailableLectures()
                .switchIfEmpty(
                        Flux.just()
                ).flatMap(
                        lecture -> Flux.just(
                                    ResponseLectureDto.builder()
                                            .id(lecture.getId())
                                            .title(lecture.getTitle())
                                            .description(lecture.getDescription())
                                            .instructorId(lecture.getInstructorId())
                                            .venueId(lecture.getVenueId())
                                            .maxAttendees(lecture.getMaxAttendees())
                                            .currentAttendees(lecture.getCurrentAttendees())
                                            .build()
                            )
                );
    }

    @PutMapping("/lecture/application/{applicationId}/cancel")
    public Mono<ResponseEntity<Object>> cancelApplication(@PathVariable Long applicationId) {
        return lectureApplicationService.cancelApplication(applicationId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/lecture/popular")
    public Flux<ResponseLectureDto> getPopularLectures() {
        return lectureApplicationService.getPopularLectures()
        .switchIfEmpty(
                Flux.just()
        ).flatMap(
                lecture -> Flux.just(
                        ResponseLectureDto.builder()
                                .id(lecture.getId())
                                .title(lecture.getTitle())
                                .description(lecture.getDescription())
                                .instructorId(lecture.getInstructorId())
                                .venueId(lecture.getVenueId())
                                .maxAttendees(lecture.getMaxAttendees())
                                .currentAttendees(lecture.getCurrentAttendees())
                                .build()
                )
        );
    }
}
