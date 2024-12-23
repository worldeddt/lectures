package api.lectures.services;


import api.lectures.entities.LectureApplication;
import api.lectures.enums.LectureApplicationStatus;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LectureApplicationService {

    private final LectureApplicationRepository lectureApplicationRepository;
    private final LectureRepository lectureRepository;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Transactional
    public Mono<Void> applyForLecture(Long lectureId, Long attenderId) {
        String lockKey = "lecture:" + lectureId;

        return redisTemplate.opsForValue().setIfAbsent(lockKey, "LOCK", Duration.ofSeconds(5))
                .flatMap(isLocked -> {
                    if (Boolean.FALSE.equals(isLocked)) {
                        return Mono.error(new IllegalStateException("Resource is locked by another process"));
                    }

                    return lectureRepository.findById(lectureId)
                            .switchIfEmpty(Mono.error(new IllegalArgumentException("Lecture not found")))
                            .flatMap(lecture -> {
                                if (lecture.getCurrentAttendees() >= lecture.getMaxAttendees()) {
                                    return Mono.error(ErrorCode.LECTURE_IS_FULL.build());
                                }
                                return lectureApplicationRepository.findByLectureIdAndAttenderId(lectureId, attenderId)
                                        .flatMap(existing ->
                                            Mono.error(ErrorCode.ATTENDER_DUPLICATED.build())
                                        )
                                        .switchIfEmpty(Mono.defer(() -> {
                                            lecture.setCurrentAttendees(lecture.getCurrentAttendees() + 1);
                                            LectureApplication application = new LectureApplication();
                                            application.setLectureId(lectureId);
                                            application.setAttenderId(attenderId);
                                            application.setStatus(LectureApplicationStatus.REGISTER.name());
                                            return lectureRepository.save(lecture)
                                                    .then(lectureApplicationRepository.save(application))
                                                    .then();
                                        }));
                            });
                })
                .doFinally(signal -> redisTemplate.delete(lockKey).subscribe()).then();
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

    public Flux<LectureApplication> getApplicationsByAttenderNumber(String attenderNumber) {
        return lectureApplicationRepository.findByAttenderNumber(attenderNumber);
    }
}
