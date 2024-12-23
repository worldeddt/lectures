package api.lectures.services;


import api.lectures.controller.dto.CreateInstructorDto;
import api.lectures.entities.Instructor;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.InstructorRepository;
import api.lectures.services.dto.InstructorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Instructor> create(CreateInstructorDto createInstructorDto) {
        return instructorRepository.save(
                Instructor.builder()
                        .name(createInstructorDto.getName())
                        .phone(createInstructorDto.getPhone())
                        .build()
        );
    }

    public Mono<InstructorDto> findInstructorById(Long id) {
        return instructorRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(ErrorCode.NOT_FOUND_INSTRUCTOR.build())
                )
                .flatMap(instructor -> Mono.just(
                                InstructorDto.builder()
                                        .id(instructor.getId())
                                        .name(instructor.getName())
                                        .phone(instructor.getPhone())
                                        .build()
                        )
                );
    }

    public Mono<List<InstructorDto>> findAllInstructors() {
        return instructorRepository.findAll()
                .collectList()
                .flatMap(
                        instructors -> {
                            if (instructors.isEmpty()) {
                                return Mono.empty();
                            } else {
                                List<InstructorDto> instructorDtos = new ArrayList<>();

                                for (Instructor instructor : instructors) {
                                    instructorDtos.add(
                                            InstructorDto.builder()
                                                    .id(instructor.getId())
                                                    .name(instructor.getName())
                                                    .phone(instructor.getPhone())
                                                    .build()
                                    );
                                }

                                return Mono.just(instructorDtos);
                            }
                        }
                );
    }
}
