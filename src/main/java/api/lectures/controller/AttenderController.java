package api.lectures.controller;


import api.lectures.entities.Attender;
import api.lectures.services.AttenderService;
import api.lectures.services.dto.AttenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/attender")
@RequiredArgsConstructor
public class AttenderController {

    private final AttenderService attenderService;

    @PostMapping("")
    public Mono<Attender> createAttender(@RequestBody AttenderDto attenderDto) {
        return attenderService.create(attenderDto);
    }
}
