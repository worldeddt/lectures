package api.lectures.services;


import api.lectures.controller.dto.ResponseLectureApplicationDto;
import api.lectures.controller.dto.ResponseLectureDto;
import api.lectures.entities.LectureApplication;
import api.lectures.enums.LectureApplicationStatus;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LectureApplicationService {

    private final LectureApplicationRepository lectureApplicationRepository;
    private final LectureRepository lectureRepository;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> applyForLecture(Long lectureId, Long attenderId) {
        String lockKey = "lecture:" + lectureId;

        return redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCK", Duration.ofSeconds(3))
                .doOnSuccess(isLocked -> log.info("Redis Lock set: {}", isLocked))
                .flatMap(isLocked -> {
                    if (Boolean.FALSE.equals(isLocked)) {
                        return Mono.error(ErrorCode.RESOURCE_IS_LOCKED.build());
                    }
                    return lectureRepository.findById(lectureId)
                            .doOnNext(lecture -> log.info("Lecture found: {}", lecture))
                            .switchIfEmpty(Mono.error(ErrorCode.NOT_FOUND_LECTURE.build()))
                            .flatMap(lecture -> {
                                if (lecture.getCurrentAttendees() >= lecture.getMaxAttendees()) {
                                    return Mono.error(ErrorCode.LECTURE_IS_FULL.build());
                                }
                                return lectureApplicationRepository.findByLectureIdAndAttenderId(lectureId, attenderId)
                                        .doOnNext(existing -> log.info("Existing application found: {}", existing))
                                        .flatMap(existing -> Mono.error(ErrorCode.ATTENDER_DUPLICATED.build()))
                                        .switchIfEmpty(Mono.defer(() -> {
                                            lecture.setCurrentAttendees(lecture.getCurrentAttendees() + 1);
                                            LectureApplication application = new LectureApplication();
                                            application.setLectureId(lectureId);
                                            application.setAttenderId(attenderId);
                                            application.setStatus(LectureApplicationStatus.REGISTER.name());
                                            return lectureRepository.save(lecture)
                                                    .doOnSuccess(savedLecture -> log.info("Lecture saved: {}", savedLecture))
                                                    .then(lectureApplicationRepository.save(application)
                                                            .onErrorResume( e -> {
                                                                log.info(e.getMessage());
                                                                return null;
                                                            })
                                                            .doOnSuccess(savedApplication -> log.info("Application saved: {}", savedApplication)));
                                        }));
                            });
                })
                .doFinally(signal -> redisTemplate.delete(lockKey)
                        .doOnSuccess(deleted -> log.info("Lock deleted for key: {}", lockKey))
                        .subscribe())
                .then();
    }

    @Transactional
    public Mono<Void> cancelApplication(Long applicationId) {
        return lectureApplicationRepository.findById(applicationId)
                .switchIfEmpty(Mono.error(ErrorCode.APPLICATION_NOT_FOUND.build()))
                .flatMap(application -> lectureRepository.findById(application.getLectureId())
                        .flatMap(lecture -> {
                            lecture.setCurrentAttendees(Math.max(lecture.getCurrentAttendees() - 1, 0)); // 신청자 수 감소
                            application.setStatus(LectureApplicationStatus.CANCELED.name()); // 상태를 'CANCELED'로 변경
                            return lectureRepository.save(lecture)
                                    .then(lectureApplicationRepository.save(application)) // 상태 변경 저장
                                    .then();
                        }));
    }

    public Mono<List<ResponseLectureApplicationDto>> getApplicationsByAttenderNumber(String attenderNumber) {
        return lectureApplicationRepository.findByAttenderNumber(attenderNumber)
                .map(lectureApplication -> ResponseLectureApplicationDto.builder()
                        .id(lectureApplication.getId())
                        .lectureId(lectureApplication.getLectureId())
                        .status(lectureApplication.getStatus())
                        .attenderId(lectureApplication.getAttenderId())
                        .build())
                .collectList();
    }

    public Mono<List<ResponseLectureDto>> getPopularLectures() {
        return lectureApplicationRepository.findTopPopularLecturesForLast3Days()
                .map(lecture -> ResponseLectureDto.builder()
                        .id(lecture.getId())
                        .title(lecture.getTitle())
                        .description(lecture.getDescription())
                        .instructorId(lecture.getInstructorId())
                        .venueId(lecture.getVenueId())
                        .maxAttendees(lecture.getMaxAttendees())
                        .currentAttendees(lecture.getCurrentAttendees())
                        .build())
                .collectList();
    }
}
