package api.lectures.services;


import api.lectures.entities.Instructor;
import api.lectures.entities.Venue;
import api.lectures.repository.InstructorRepository;
import api.lectures.repository.LectureRepository;
import api.lectures.repository.VenueRepository;
import api.lectures.services.dto.InstructorDto;
import api.lectures.services.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final InstructorRepository instructorRepository;
    private final VenueRepository venueRepository;

    public Mono<LectureDto> getLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .flatMap(lecture -> lectureRepository.findById(lecture.getVenueId())
                        .map(venue -> new LectureDto(lecture, venue)));
    }


    public Flux<List<LectureDto>> findAllLectures() {
        return lectureRepository.findAll()
                .flatMap(lecture -> {
                    Mono<Instructor> instructorMono = instructorRepository.findById(lecture.getInstructorId());
                    Mono<Venue> venueMono = venueRepository.findById(lecture.getVenueId());

                    InstructorDto instructorDto = null;

                    instructorMono.flatMap(instructor -> {
                        if (instructor != null) {
                            InstructorDto.builder()
                                    .id(instructor.getId())
                                    .phone(instructor.getPhone())
                                    .phone(instructor.getPhone())
                                    .build();
                        }

                        return Mono.just(instructor);
                    });


                    if (instructorMono != null) {

                    }

                    LectureDto.builder()
                            .id(lecture.getId())
                            .title(lecture.getTitle())
                            .instructor(
                                    instructorDto
                            )
                    return instructorRepository.findById(lecture.getInstructorId());
                })
    }

    public Flux<List<LectureDto>> favoriteLectures() {
        return null;
    }

    public Mono<LectureDto> registerLecture(LectureDto lectureDto) {
        lectureRepository.sav
    }
}
