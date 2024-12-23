package api.lectures.service;

import api.lectures.entities.Lecture;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.services.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class BackOfficeLectureServiceTest {

    @Autowired
    private LectureService lectureService;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private LectureApplicationRepository lectureApplicationRepository;

    @Test
    void shouldReturnAllLectures() {
        List<Lecture> lectures = List.of(
            new Lecture(1L,
                    "Reactive Programming",
                    "Learn Reactive Programming",
                    50, 30,
                    LocalDateTime.of(2024, 12, 30, 10, 0), 1L, 1L),
            new Lecture(2L,
                    "Spring WebFlux",
                    "Introduction to WebFlux",
                    100, 80,
                    LocalDateTime.of(2024, 12, 25, 14, 0), 2L, 2L)
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
//        List<String> attenders = List.of("12345", "54321");
//
//        when(lectureApplicationRepository.findAllByLectureId(1L))
//                .thenReturn(Flux.fromIterable(attenders));
//
//        StepVerifier.create(lectureService.getAttendersByLectureId(1L))
//                .expectNext("12345")
//                .expectNext("54321")
//                .verifyComplete();
//
//        verify(lectureApplicationRepository, times(1)).findAttenderIdsByLectureId(1L);
    }
}