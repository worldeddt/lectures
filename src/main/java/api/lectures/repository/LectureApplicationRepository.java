package api.lectures.repository;


import api.lectures.entities.LectureApplication;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LectureApplicationRepository extends ReactiveCrudRepository<LectureApplication, Long> {
    // 특정 강의 ID에 해당하는 모든 수강 신청 조회
    @Query("SELECT * FROM lecture_application WHERE lecture_id = :lectureId")
    Flux<LectureApplication> findAllByLectureId(Long lectureId);

    @Query("SELECT * FROM lecture_application WHERE lecture_id = :lectureId AND attender_id = :attenderId")
    Mono<LectureApplication> findByLectureIdAndAttenderId(Long lectureId, Long attenderId);

    @Query("""
        SELECT * FROM lecture_application
        WHERE attender_id IN (
        SELECT attender_id 
        FROM attender 
        WHERE attender_number = :attenderNumber
                                         ) AND status = 'REGISTER';
""")
    Flux<LectureApplication> findByAttenderNumber(String attenderNumber);
}
