package api.lectures.services;


import api.lectures.repository.LectureRepository;
import api.lectures.services.dto.LectureDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    public Mono<LectureWithVenue> getLectureWithVenue(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .flatMap(lecture -> venueRepository.findById(lecture.getVenueId())
                        .map(venue -> new LectureWithVenue(lecture, venue)));
    }


    public Flux<List<LectureDto>> findAllLectures() {
        return lectureRepository.findAll()
                .flatMap(lecture -> {
                    List<LectureDto> lectureDtos = new ArrayList<>();
                    lectureDtos.add(

                    )
                })
    }

}
