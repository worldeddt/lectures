package api.lectures.service;


import api.lectures.entities.Lecture;
import api.lectures.entities.LectureApplication;
import api.lectures.enums.LectureStatus;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.services.LectureApplicationService;
import api.lectures.services.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FrontApiServiceTest {

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureApplicationService lectureApplicationService;

    @MockitoBean
    private LectureRepository lectureRepository;

    @MockitoBean
    private LectureApplicationRepository lectureApplicationRepository;

    //강연 신청 가능 목록 테스트
    @Test
    void shouldReturnAvailableLectures() {
        LocalDateTime now = LocalDateTime.now();

        // 테스트 데이터 생성
        List<Lecture> lectures = List.of(
                new Lecture(1L,
                        "Reactive Programming",
                        "Learn Reactive Programming",
                        50,
                        30,
                        now.plusDays(2),
                        1L, 1L,
                        LectureStatus.REGISTER),
                new Lecture(2L,
                        "Spring WebFlux",
                        "Introduction to WebFlux",
                        100,
                        80,
                        now.plusDays(6),
                        2L, 2L,
                        LectureStatus.REGISTER),
                new Lecture(3L,
                        "Invalid Lecture",
                        "",
                        100,
                        0,
                        now.minusDays(3),
                        3L, 3L,
                        LectureStatus.REGISTER),
                new Lecture(4L,
                        "Another Invalid Lecture",
                        "ㅇㅇㅇ",
                        100,
                        0,
                        now.plusDays(5),
                        4L, 4L,
                        LectureStatus.REGISTER)
        );

        // Mocking
        when(lectureRepository.findAllAvailableLectures())
                .thenReturn(Flux.fromIterable(lectures));

        // 실행 및 검증
        StepVerifier.create(lectureService.getAvailableLectures())
                .expectNextMatches(lecture -> lecture.getId() == 1L && lecture.getTitle().equals("Reactive Programming"))
                .expectNextMatches(lecture -> lecture.getId() == 2L && lecture.getTitle().equals("Spring WebFlux"))
                .expectNextMatches(lecture -> lecture.getId() == 3L && lecture.getTitle().equals("Invalid Lecture"))
                .expectNextMatches(lecture -> lecture.getId() == 4L && lecture.getTitle().equals("Another Invalid Lecture"))
                .verifyComplete();

        // Repository 호출 검증
        verify(lectureRepository, times(1)).findAllAvailableLectures();
    }

    //신청 내역 조회 테스트
    @Test
    void shouldReturnApplicationsForAttender() {
        String attenderNumber = "12345";

        List<LectureApplication> lectureApplications = List.of(
                new LectureApplication(1L, 1L, 1L, "REGISTER"),
                new LectureApplication(2L, 2L, 1L, "REGISTER")
        );

        // Mock Repository
        when(lectureApplicationRepository.findByAttenderNumber(attenderNumber))
                .thenReturn(Flux.fromIterable(lectureApplications));

        // Execute & Verify
        StepVerifier.create(lectureApplicationService.getApplicationsByAttenderNumber(attenderNumber))
                .expectNextMatches(responseDtos -> {
                    assertEquals(2, responseDtos.size());
                    assertEquals(1L, responseDtos.get(0).getId());
                    assertEquals("REGISTER", responseDtos.get(0).getStatus());
                    assertEquals(2L, responseDtos.get(1).getId());
                    assertEquals("REGISTER", responseDtos.get(1).getStatus());
                    return true;
                })
                .verifyComplete();

        // Verify Repository Interaction
        verify(lectureApplicationRepository, times(1)).findByAttenderNumber(attenderNumber);
    }

    //실시간 인기 강연 테스트
    @Test
    void shouldReturnTopPopularLectures() {
        // Mock 데이터
        List<Lecture> popularLectures = List.of(
                new Lecture(1L,
                        "Reactive Programming",
                        "Learn Reactive Programming",
                        50, 30,
                        LocalDateTime.now().minusDays(1),
                        1L, 1L, LectureStatus.REGISTER),
                new Lecture(2L,
                        "Spring WebFlux",
                        "Introduction to WebFlux",
                        100, 80,
                        LocalDateTime.now().minusDays(2),
                        2L, 2L, LectureStatus.REGISTER)
        );

        // Mocking
        when(lectureApplicationRepository.findTopPopularLecturesForLast3Days())
                .thenReturn(Flux.fromIterable(popularLectures));

        // 실행 및 검증
        StepVerifier.create(lectureApplicationService.getPopularLectures())
                .expectNextMatches(lectures -> {
                    assertEquals(2, lectures.size());

                    // 첫 번째 인기 강연 검증
                    assertEquals(1L, lectures.get(0).getId());
                    assertEquals("Reactive Programming", lectures.get(0).getTitle());
                    assertEquals(30, lectures.get(0).getCurrentAttendees());

                    // 두 번째 인기 강연 검증
                    assertEquals(2L, lectures.get(1).getId());
                    assertEquals("Spring WebFlux", lectures.get(1).getTitle());
                    assertEquals(80, lectures.get(1).getCurrentAttendees());

                    return true;
                })
                .verifyComplete();

        // Repository 호출 검증
        verify(lectureApplicationRepository, times(1)).findTopPopularLecturesForLast3Days();
    }
}
