package api.lectures.services;


import api.lectures.entities.Instructor;
import api.lectures.entities.Venue;
import api.lectures.repository.InstructorRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.repository.VenueRepository;
import api.lectures.services.dto.InstructorDto;
import api.lectures.services.dto.LectureDto;
import api.lectures.services.dto.VenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final InstructorRepository instructorRepository;
    private final VenueRepository venueRepository;

    public Mono<LectureDto> getLectureDetails(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .flatMap(lecture -> Mono.zip(
                        Mono.just(lecture),
                        instructorRepository.findById(lecture.getInstructorId())
                                .switchIfEmpty(Mono.just(new Instructor())), // 강연자 정보 없을 경우 기본값
                        venueRepository.findById(lecture.getVenueId())
                                .switchIfEmpty(Mono.just(new Venue())) // 강연장 정보 없을 경우 기본값
                ).map(tuple -> LectureDto.builder()
                        .id(tuple.getT1().getId())
                        .title(tuple.getT1().getTitle())
                        .instructor(
                                Optional.ofNullable(tuple.getT2())
                                        .map(instructor -> InstructorDto.builder()
                                                .id(instructor.getId())
                                                .phone(instructor.getPhone())
                                                .name(instructor.getName())
                                                .build()
                                        ).orElse(null) // null-safe 처리
                        )
                        .venue(
                                Optional.ofNullable(tuple.getT3())
                                        .map(venue -> VenueDto.builder()
                                                .id(venue.getId())
                                                .address(venue.getAddress())
                                                .name(venue.getName())
                                                .seatCount(venue.getSeatCount())
                                                .build()
                                        ).orElse(null) // null-safe 처리
                        )
                        .build()
                ))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Lecture not found"))); // 강의가 없을 경우 처리
    }

    public Mono<List<LectureDto>> findLectureAll() {
        return lectureRepository.findAll()
                .flatMap(lecture -> Mono.zip(
                        Mono.just(lecture),
                        instructorRepository.findById(lecture.getInstructorId())
                                .switchIfEmpty(Mono.just(new Instructor())), // 기본값 제공
                        venueRepository.findById(lecture.getVenueId())
                                .switchIfEmpty(Mono.just(new Venue())) // 기본값 제공
                ).map(tuple -> LectureDto.builder()
                        .id(tuple.getT1().getId())
                        .title(tuple.getT1().getTitle())
                        .instructor(
                                Optional.ofNullable(tuple.getT2())
                                        .map(instructor -> InstructorDto.builder()
                                                .id(instructor.getId())
                                                .phone(instructor.getPhone())
                                                .name(instructor.getName())
                                                .build()
                                        ).orElse(null) // null-safe 처리
                        )
                        .venue(
                                Optional.ofNullable(tuple.getT3())
                                        .map(venue -> VenueDto.builder()
                                                .id(venue.getId())
                                                .address(venue.getAddress())
                                                .name(venue.getName())
                                                .seatCount(venue.getSeatCount())
                                                .build()
                                        ).orElse(null) // null-safe 처리
                        )
                        .build()
                ))
                .collectList(); // Flux를 Mono<List>로 변환
    }



    public Flux<List<LectureDto>> favoriteLectures() {
        return null;
    }

    public Mono<LectureDto> registerLecture(LectureDto lectureDto) {
        return null;
    }
}
