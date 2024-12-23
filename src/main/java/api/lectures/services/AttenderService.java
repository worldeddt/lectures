package api.lectures.services;


import api.lectures.entities.Attender;
import api.lectures.exception.ErrorCode;
import api.lectures.repository.AttenderRepository;
import api.lectures.services.dto.AttenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

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
                        .build()
        );
    }

    public Mono<AttenderDto> findAttenderById(Long id) {
        return attenderRepository.findById(id)
                .switchIfEmpty(
                        Mono.error(ErrorCode.INVALID_ATTENDER_ID.build())
                )
                .flatMap(attender -> {
                    return Mono.just(
                        AttenderDto.builder()
                                .tel(attender.getTel())
                                .attenderNumber(attender.getAttenderNumber())
                                .tel(attender.getTel())
                                .build()
                    );
                });
    }
}
