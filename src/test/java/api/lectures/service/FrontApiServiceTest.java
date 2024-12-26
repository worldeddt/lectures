package api.lectures.service;


import api.lectures.LecturesApplication;
import api.lectures.entities.Lecture;
import api.lectures.entities.LectureApplication;
import api.lectures.enums.LectureApplicationStatus;
import api.lectures.enums.LectureStatus;
import api.lectures.repository.InstructorRepository;
import api.lectures.repository.LectureApplicationRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.repository.VenueRepository;
import api.lectures.services.LectureApplicationService;
import api.lectures.services.LectureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    @Qualifier("reactiveStringRedisTemplate")
    private ReactiveRedisTemplate<String, String> redisTemplate;
    @Autowired
    private LecturesApplication lecturesApplication;

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

    /**
     Instructor mockInstructor =
     Instructor.builder()
     .id(2L)
     .name("david")
     .phone("00011112222")
     .build();

     Venue mockVenue = Venue
     .builder()
     .id(1L)
     .name("abc")
     .address("본관 2층")
     .seatCount(20L).build();
     */

    //강연 신청
    @Test
    void applyLectureTest() {
        //given
        Long attenderId = 1L;
        Long lectureId = 2L;

        Lecture mockLecture =
                Lecture.builder()
                    .id(lectureId)
                    .title("2강의")
                    .maxAttendees(20)
                    .currentAttendees(0)
                    .description("양자터널링에 관한 강의")
                    .status(LectureStatus.REGISTER)
                    .startTime(LocalDateTime.now().minusDays(1))
                    .instructorId(2L)
                    .venueId(1L)
                    .build();

        LectureApplication mockApplication =
                LectureApplication.builder()
                .id(1L)
                .lectureId(mockLecture.getId())
                .attenderId(attenderId)
                .status(LectureApplicationStatus.REGISTER.name())
                .build();

        when(lectureRepository.findById(lectureId))
                .thenReturn(Mono.just(mockLecture));
        when(lectureApplicationRepository.findByLectureIdAndAttenderId(lectureId, attenderId))
                .thenReturn(Mono.empty());
        when(lectureRepository.save(any(Lecture.class)))
                .thenReturn(Mono.just(mockLecture));
        when(lectureApplicationRepository.save(any(LectureApplication.class)))
                .thenReturn(Mono.just(mockApplication));

        //then
        // Execute & Verify
        StepVerifier.create(
                lectureApplicationService.applyForLecture(lectureId, attenderId))
                .expectNextMatches( object -> {
                    try {
                        String s = new ObjectMapper().writeValueAsString(object);
                        LectureApplication lectureApplication = new ObjectMapper().readValue(s, LectureApplication.class);
                        assertEquals(1L, lectureApplication.getId());
                        assertEquals(attenderId, lectureApplication.getAttenderId());
                        assertEquals(lectureId, lectureApplication.getLectureId());
                        assertEquals(LectureStatus.REGISTER.name(), lectureApplication.getStatus());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    return true;
                })
                .verifyComplete();

        verify(lectureRepository, times(1)).findById(lectureId);
        verify(lectureApplicationRepository, times(1)).
                findByLectureIdAndAttenderId(lectureId, attenderId);
        verify(lectureRepository, times(1)).save(any(Lecture.class));
        verify(lectureApplicationRepository, times(1)).save(any(LectureApplication.class));

    }
}
