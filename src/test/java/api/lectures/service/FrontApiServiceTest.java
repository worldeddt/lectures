package api.lectures.service;


import api.lectures.entities.Lecture;
import api.lectures.entities.LectureApplication;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.services.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class FrontApiServiceTest {

    @Autowired
    private LectureService lectureService;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private LectureApplicationRepository lectureApplicationRepository;

    //강연 신청 가능 목록 테스트
    @Test
    void shouldReturnAvailableLectures() {
        List<Lecture> lectures = List.of(
                new Lecture(1L, "Reactive Programming", "Learn Reactive Programming", 50, 30, LocalDateTime.of(2024, 12, 30, 10, 0), 1L, 1L),
                new Lecture(2L, "Spring WebFlux", "Introduction to WebFlux", 100, 80, LocalDateTime.of(2024, 12, 25, 14, 0), 2L, 2L)
        );

        when(lectureRepository.findAllAvailableLectures())
                .thenReturn(Flux.fromIterable(lectures));

        StepVerifier.create(lectureService.getAvailableLectures())
                .expectNextMatches(lecture -> lecture.getTitle().equals("Reactive Programming"))
                .expectNextMatches(lecture -> lecture.getTitle().equals("Spring WebFlux"))
                .verifyComplete();

        verify(lectureRepository, times(1)).findAllAvailableLectures();
    }


    //강연 신청 테스트
    @Test
    void shouldThrowErrorWhenDuplicateApplication() {
//        LectureApplication existingApplication = new LectureApplication(1L, 1L, "Pending");
//
//        when(lectureApplicationRepository.findByLectureIdAndAttenderId(1L, "12345"))
//                .thenReturn(Mono.just(existingApplication));
//
//        Mono<Void> result = lectureService.createLecture(1L, "12345");
//
//        StepVerifier.create(result)
//                .expectErrorMatches(throwable -> throwable instanceof IllegalStateException && throwable.getMessage().equals("Already applied"))
//                .verify();
//
//        verify(lectureApplicationRepository, times(1)).findByLectureIdAndAttenderId(1L, "12345");
    }

    //신청 내역 조회 테스트
    @Test
    void shouldReturnApplicationsForAttender() {
//        List<LectureApplication> applications = List.of(
//                new LectureApplication(1L, 1L, "Pending"),
//                new LectureApplication(2L, 2L, "Pending")
//        );
//
//        when(lectureApplicationRepository.findByAttenderId("12345"))
//                .thenReturn(Flux.fromIterable(applications));
//
//        StepVerifier.create(lectureService.getApplicationsByAttenderId("12345"))
//                .expectNextMatches(app -> app.getLectureId().equals(1L))
//                .expectNextMatches(app -> app.getLectureId().equals(2L))
//                .verifyComplete();
//
//        verify(lectureApplicationRepository, times(1)).findByAttenderId("12345");
    }

    //신청 취소 테스트
    @Test
    void shouldCancelApplicationSuccessfully() {
//        LectureApplication application = new LectureApplication(1L, 1L, "Pending");
//
//        when(lectureApplicationRepository.findById(1L))
//                .thenReturn(Mono.just(application));
//        when(lectureApplicationRepository.save(any()))
//                .thenReturn(Mono.just(application));
//
//        Mono<Void> result = lectureService.cancelApplication(1L);
//
//        StepVerifier.create(result)
//                .verifyComplete();
//
//        verify(lectureApplicationRepository, times(1)).findById(1L);
//        verify(lectureApplicationRepository, times(1)).save(any());
    }

    //실시간 인기 강연 테스트
    @Test
    void shouldReturnTopPopularLectures() {
//        List<Lecture> popularLectures = List.of(
//                new Lecture(1L, "Reactive Programming", "Learn Reactive Programming", 50, 30, LocalDateTime.of(2024, 12, 30, 10, 0), 1L, 1L),
//                new Lecture(2L, "Spring WebFlux", "Introduction to WebFlux", 100, 80, LocalDateTime.of(2024, 12, 25, 14, 0), 2L, 2L)
//        );
//
//        when(lectureRepository.findTopPopularLecturesForLast3Days())
//                .thenReturn(Flux.fromIterable(popularLectures));
//
//        StepVerifier.create(lectureService.getPopularLectures())
//                .expectNextCount(2)
//                .verifyComplete();
//
//        verify(lectureRepository, times(1)).findTopPopularLecturesForLast3Days();
    }

    // 강연 등록 테스트
    @Test
    void shouldRegisterLectureSuccessfully() {
//        Lecture lecture = new Lecture(null, "Reactive Programming", "Learn Reactive Programming", 50, 0, LocalDateTime.of(2024, 12, 30, 10, 0), 1L, 1L);
//
//        when(lectureRepository.save(any()))
//                .thenReturn(Mono.just(lecture));
//
//        StepVerifier.create(lectureService.registerLecture(lecture))
//                .expectNextMatches(savedLecture -> savedLecture.getTitle().equals("Reactive Programming"))
//                .verifyComplete();
//
//        verify(lectureRepository, times(1)).save(any());
    }
}
