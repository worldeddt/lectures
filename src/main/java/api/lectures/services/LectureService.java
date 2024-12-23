package api.lectures.services;


import api.lectures.controller.dto.CreateLectureDto;
import api.lectures.entities.Instructor;
import api.lectures.entities.Lecture;
import api.lectures.entities.Venue;
import api.lectures.enums.LectureApplicationStatus;
import api.lectures.enums.LectureStatus;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.*;
import api.lectures.services.dto.AttenderDto;
import api.lectures.services.dto.InstructorDto;
import api.lectures.services.dto.LectureDto;
import api.lectures.services.dto.VenueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final InstructorRepository instructorRepository;
    private final VenueRepository venueRepository;
    private final LectureApplicationRepository lectureApplicationRepository;
    private final AttenderRepository attenderRepository;

    public Mono<LectureDto> getLectureDetails(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .flatMap(lecture -> Mono.zip(
                        Mono.just(lecture),
                        instructorRepository.findById(lecture.getInstructorId())
                                .switchIfEmpty(Mono.just(new Instructor())),
                        venueRepository.findById(lecture.getVenueId())
                                .switchIfEmpty(Mono.just(new Venue()))
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
                                        ).orElse(null)
                        )
                        .venue(
                                Optional.ofNullable(tuple.getT3())
                                        .map(venue -> VenueDto.builder()
                                                .id(venue.getId())
                                                .address(venue.getAddress())
                                                .name(venue.getName())
                                                .seatCount(venue.getSeatCount())
                                                .build()
                                        ).orElse(null)
                        )
                        .build()
                ))
                .switchIfEmpty(Mono.error(ErrorCode.NOT_FOUND_LECTURE.build()));
    }

    public Mono<List<LectureDto>> getLectureAll() {
        return lectureRepository.findAll()
                .flatMap(lecture -> Mono.zip(
                        Mono.just(lecture),
                        instructorRepository.findById(lecture.getInstructorId())
                                .switchIfEmpty(Mono.just(new Instructor())),
                        venueRepository.findById(lecture.getVenueId())
                                .switchIfEmpty(Mono.just(new Venue()))
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
                                        ).orElse(null)
                        )
                        .venue(
                                Optional.ofNullable(tuple.getT3())
                                        .map(venue -> VenueDto.builder()
                                                .id(venue.getId())
                                                .address(venue.getAddress())
                                                .name(venue.getName())
                                                .seatCount(venue.getSeatCount())
                                                .build()
                                        ).orElse(null)
                        )
                        .build()
                ))
                .collectList();
    }

    public Mono<List<AttenderDto>> getAttendersByLectureId(Long lectureId) {
        return lectureApplicationRepository.findAllByLectureId(lectureId)
                .filter(lectureApplication ->
                        lectureApplication.getStatus().equals(LectureApplicationStatus.REGISTER.name()))
                .flatMap(application ->
                        attenderRepository.findById(application.getAttenderId())
                                .map(attender -> AttenderDto.builder()
                                        .id(attender.getId())
                                        .name(attender.getName())
                                        .tel(attender.getTel())
                                        .attenderNumber(attender.getAttenderNumber())
                                        .build()
                                )).collectList();
    }

    public Mono<Lecture> createLecture(CreateLectureDto request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 강연자와 강연장의 유효성 검증
        return instructorRepository.findById(request.getInstructorId())
                .switchIfEmpty(
                        Mono.error(
                                ErrorCode.INVALID_VENUE_ID.build()
                        )
                )
                .flatMap(instructor -> venueRepository.findById(request.getVenueId())
                        .switchIfEmpty(
                                Mono.error(
                                        ErrorCode.INVALID_VENUE_ID.build()
                                )
                        )
                        .flatMap(venue -> {

                            if (venue.getSeatCount() < request.getSeatCount()) {
                                return Mono.error(ErrorCode.OVER_SEAT_COUNT_REGISTERING.build());
                            }

                            // 유효성 검증 성공 시 Lecture 생성
                            Lecture lecture = new Lecture();
                            lecture.setTitle(request.getTitle());
                            lecture.setDescription(request.getDescription());
                            lecture.setStartTime(LocalDateTime.parse(request.getStartTime(), formatter));
                            lecture.setInstructorId(request.getInstructorId());
                            lecture.setVenueId(request.getVenueId());
                            lecture.setMaxAttendees(request.getSeatCount());
                            lecture.setStatus(LectureStatus.REGISTER);
                            return lectureRepository.save(lecture);
                        })
                );
    }

    public Flux<Lecture> getAvailableLectures() {
        return lectureRepository.findAllAvailableLectures();
    }
}
