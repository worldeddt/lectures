package api.lectures.services;


import api.lectures.entities.Attender;
import api.lectures.repository.AttenderRepository;
import api.lectures.services.dto.AttenderDto;
import lombok.RequiredArgsConstructor;
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
}
