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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/front")
@RequiredArgsConstructor
public class ApiController {

    private final LectureService lectureService;
    private final LectureApplicationService lectureApplicationService;

    @Description("신청한 내역 조회(사번 입력)")
    @GetMapping("/applications/{attenderNumber}")
    public Mono<ResponseEntity<List<ResponseLectureApplicationDto>>> getApplicationsByAttenderNumber(
            @PathVariable String attenderNumber) {
        return lectureApplicationService.getApplicationsByAttenderNumber(attenderNumber)
                .collectList()
                .flatMap(lectureApplications -> {
                    if (lectureApplications.isEmpty()) {
                        return Mono.error(ErrorCode.NOT_FOUND_LECTURE.build());
                    } else {
                        List<ResponseLectureApplicationDto> responseLectureApplicationDtos = new ArrayList<>();

                        lectureApplications.forEach(lectureApplication -> {
                            responseLectureApplicationDtos.add(
                                    ResponseLectureApplicationDto.builder()
                                            .id(lectureApplication.getId())
                                            .lectureId(lectureApplication.getLectureId())
                                            .status(lectureApplication.getStatus())
                                            .attenderId(lectureApplication.getAttenderId())
                                            .build()
                            );
                        });

                        return Mono.just(ResponseEntity.ok().body(responseLectureApplicationDtos));
                    }
                });
    }

    @Description("강연 목록(신청 가능한 시점 부터 강연 시작 시간 1일 후까지 노출)")
    @GetMapping("/lectures/available")
    public Mono<ResponseEntity<List<ResponseLectureDto>>> getAvailableLectures() {
        return lectureService.getAvailableLectures()
                .collectList()
                .flatMap(
                        lectures -> {
                            if (lectures.isEmpty()) {
                                return Mono.just(ResponseEntity.noContent().build());
                            } else {
                                List<ResponseLectureDto> responseLectureDtos = new ArrayList<>();
                                for (Lecture lecture : lectures) {
                                    responseLectureDtos.add(
                                            ResponseLectureDto.builder()
                                                    .id(lecture.getId())
                                                    .title(lecture.getTitle())
                                                    .description(lecture.getDescription())
                                                    .instructorId(lecture.getInstructorId())
                                                    .venueId(lecture.getVenueId())
                                                    .startTime(lecture.getStartTime())
                                                    .maxAttendees(lecture.getMaxAttendees())
                                                    .currentAttendees(lecture.getCurrentAttendees())
                                                    .build());
                                }
                                return Mono.just(ResponseEntity.ok().body(responseLectureDtos));
                            }
                        }
                );
    }

    @Description("실시간 인기 강연")
    @GetMapping("/lectures/popular")
    public Mono<ResponseEntity<List<ResponseLectureDto>>> getPopularLectures() {
        return lectureApplicationService.getPopularLectures()
                .collectList()
                .flatMap(
                        lectures -> {
                            if (lectures.isEmpty()) {
                                return Mono.just(ResponseEntity.noContent().build());
                            } else {
                                List<ResponseLectureDto> responseLectureDtos = new ArrayList<>();
                                for (Lecture lecture : lectures) {
                                    responseLectureDtos.add(
                                            ResponseLectureDto.builder()
                                                    .id(lecture.getId())
                                                    .title(lecture.getTitle())
                                                    .description(lecture.getDescription())
                                                    .instructorId(lecture.getInstructorId())
                                                    .venueId(lecture.getVenueId())
                                                    .maxAttendees(lecture.getMaxAttendees())
                                                    .currentAttendees(lecture.getCurrentAttendees())
                                                    .build());
                                }
                                return Mono.just(ResponseEntity.ok().body(responseLectureDtos));
                            }
                        }
                );
    }

    @Description("신청한 강연 취소")
    @PutMapping("/lecture/application/{applicationId}/cancel")
    public Mono<ResponseEntity<Object>> cancelApplication(@PathVariable Long applicationId) {
        return lectureApplicationService.cancelApplication(applicationId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @Description("강연 신청(사번 입력,같은 강연 중복 신청 제한)")
    @PostMapping("/lecture/{lectureId}/apply")
    public Mono<ResponseEntity<Object>> applyForLecture(@PathVariable Long lectureId, @RequestParam Long attenderId) {
        return lectureApplicationService.applyForLecture(lectureId, attenderId)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }
}
