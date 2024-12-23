package api.lectures.controller;


import api.lectures.controller.dto.ResponseLectureApplicationDto;
import api.lectures.controller.dto.ResponseLectureDto;
import api.lectures.exception.ErrorCode;
import api.lectures.services.LectureApplicationService;
import api.lectures.services.LectureService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/applications/{attenderNumber}")
    public Flux<ResponseLectureApplicationDto> getApplicationsByAttenderId(
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

    @PostMapping("/{lectureId}/apply")
    public Mono<ResponseEntity<Object>> applyForLecture(@PathVariable Long lectureId, @RequestParam Long attenderId) {
        return lectureApplicationService.applyForLecture(lectureId, attenderId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping("/available")
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
}
