package api.lectures.controller;


import api.lectures.entities.Lecture;
import api.lectures.services.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@WebFluxTest(ApiController.class)
@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Mock
    private LectureService lectureService;


    @Test
    void shouldReturnAvailableLectures() {

        /**
         private Long id;
         private String title;
         private String description;
         private int maxAttendees;
         private int currentAttendees = 0;
         private LocalDateTime startTime;
         private Long instructorId;
         private Long venueId;
         */
        List<Lecture> lectures = List.of(
                new Lecture(
                        1L,
                        "Reactive Programming",
                        "Learn Reactive Programming",
                        50,
                        30,
                        LocalDateTime.of(2024, 12, 30, 10, 0),
                        1L, 1L),
                new Lecture(
                        2L,
                        "Spring WebFlux",
                        "Introduction to WebFlux",
                        100,
                        80,
                        LocalDateTime.of(2024, 12, 25, 14, 0),
                        2L, 2L)
        );

        Mockito.when(lectureService.getAvailableLectures()).thenReturn(Flux.fromIterable(lectures));

        webTestClient.get().uri("/lectures/available")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Lecture.class)
                .hasSize(2)
                .consumeWith(response -> {
                    List<Lecture> result = response.getResponseBody();
                    assertNotNull(result);
                    assertEquals("Reactive Programming", result.get(0).getTitle());
                    assertEquals("Spring WebFlux", result.get(1).getTitle());
                    assertEquals(50, result.get(0).getMaxAttendees());
                    assertEquals(100, result.get(1).getMaxAttendees());
                });
    }
}
