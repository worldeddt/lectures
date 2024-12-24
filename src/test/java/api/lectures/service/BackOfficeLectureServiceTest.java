package api.lectures.service;

import api.lectures.entities.Attender;
import api.lectures.entities.Lecture;
import api.lectures.entities.LectureApplication;
import api.lectures.enums.LectureStatus;
import api.lectures.repository.AttenderRepository;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.services.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class BackOfficeLectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @MockitoBean
    private LectureRepository lectureRepository;

    @MockitoBean
    private LectureApplicationRepository lectureApplicationRepository;

    @MockitoBean
    private AttenderRepository attenderRepository;

    //강연목록(전체강연목록) 테스트
    @Test
    void shouldReturnAllLectures() {
        List<Lecture> lectures = List.of(
            new Lecture(1L,
                    "Reactive Programming",
                    "Learn Reactive Programming",
                    50, 30,
                    LocalDateTime.of(2024, 12, 30, 10, 0), 1L, 1L,
                    LectureStatus.REGISTER),
            new Lecture(2L,
                    "Spring WebFlux",
                    "Introduction to WebFlux",
                    100, 80,
                    LocalDateTime.of(2024, 12, 25, 14, 0), 2L, 2L,
                    LectureStatus.REGISTER),
            new Lecture(3L,
                    "Spring WebFlux",
                    "Introduction to WebFlux",
                    100, 80,
                    LocalDateTime.of(2024, 12, 25, 14, 0), 2L, 2L,
                    LectureStatus.REGISTER)
        );

        when(lectureRepository.findAll()).thenReturn(Flux.fromIterable(lectures));

        StepVerifier.create(lectureService.getLectureAll())
                .expectNextCount(2)
                .verifyComplete();

        verify(lectureRepository, times(1)).findAll();
    }


    //강연 신청자 목록 조회 테스트
    @Test
    void shouldReturnAttendersByLectureId() {

        // Mock 데이터
        Long lectureId = 1L;
        List<LectureApplication> applications = List.of(
                new LectureApplication(1L, 1L, 1L, "REGISTER"),
                new LectureApplication(2L, 2L, 1L, "REGISTER")
        );

        List<Attender> attenders = List.of(
                new Attender(1L,
                        "12314", "eddy", "EMP001", "REGISTER"),
                new Attender(2L,
                        "12321", "kidari", "EMP002", "REGISTER")
        );

        // Mocking
        when(lectureApplicationRepository.findAllByLectureId(lectureId))
                .thenReturn(Flux.fromIterable(applications));
        when(attenderRepository.findById(1L))
                .thenReturn(Mono.just(attenders.get(0)));
        when(attenderRepository.findById(2L))
                .thenReturn(Mono.just(attenders.get(1)));

        // 실행 및 검증
        StepVerifier.create(lectureService.getAttendersByLectureId(lectureId))
                .expectNextMatches(attendersList -> {
                    assertEquals(2, attendersList.size());
                    assertEquals("eddy", attendersList.get(0).getName());
                    assertEquals("12314", attendersList.get(0).getAttenderNumber());
                    assertEquals("kidari", attendersList.get(1).getName());
                    assertEquals("12321", attendersList.get(1).getAttenderNumber());
                    return true;
                })
                .verifyComplete();

        // Mock 메서드 호출 검증
        verify(lectureApplicationRepository, times(1)).findAllByLectureId(lectureId);
        verify(attenderRepository, times(1)).findById(1L);
        verify(attenderRepository, times(1)).findById(2L);
    }
}