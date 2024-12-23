package api.lectures.services;


import api.lectures.entities.Attender;
import api.lectures.enums.AttenderStatus;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.AttenderRepository;
import api.lectures.services.dto.AttenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttenderService {
    private final AttenderRepository attenderRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Attender> create(AttenderDto attenderDto) {
        return attenderRepository.save(
                Attender.builder()
                        .tel(attenderDto.getTel())
                        .name(attenderDto.getName())
                        .attenderNumber(attenderDto.getAttenderNumber())
                        .status(AttenderStatus.REGISTER.name())
                        .build()
        );
    }

    public Mono<AttenderDto> findAttenderById(Long id) {
        return attenderRepository.findById(id)
                .filter(attender -> attender.getStatus().equals(AttenderStatus.REGISTER.name()))
                .switchIfEmpty(
                        Mono.error(ErrorCode.INVALID_ATTENDER_ID.build())
                )
                .flatMap(attender -> {
                    return Mono.just(
                        AttenderDto.builder()
                                .id(attender.getId())
                                .tel(attender.getTel())
                                .attenderNumber(attender.getAttenderNumber())
                                .tel(attender.getTel())
                                .build()
                    );
                });
    }

    public Mono<List<AttenderDto>> findAttenders() {
        return attenderRepository.findAll()
                .filter(attender -> attender.getStatus().equals(AttenderStatus.REGISTER.name()))
                .collectList()
                .flatMap(attenders -> {
                    if (attenders.isEmpty()) {
                        return Mono.empty();
                    } else {
                        List<AttenderDto> attenderDtos = new ArrayList<>();

                        for (Attender attender : attenders) {
                            attenderDtos.add(
                                    AttenderDto.builder()
                                            .id(attender.getId())
                                            .tel(attender.getTel())
                                            .attenderNumber(attender.getAttenderNumber())
                                            .tel(attender.getTel())
                                            .build()
                            );
                        }

                        return Mono.just(attenderDtos);
                    }
                });
    }
}
