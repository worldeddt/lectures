package api.lectures.controller;


import api.lectures.entities.Attender;
import api.lectures.services.AttenderService;
import api.lectures.services.dto.AttenderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/attender")
@RequiredArgsConstructor
public class AttenderController {

    private final AttenderService attenderService;

    @PostMapping("")
    public Mono<Attender> createAttender(@RequestBody AttenderDto attenderDto) {
        return attenderService.create(attenderDto);
    }

    @GetMapping("/{id}")
    public Mono<AttenderDto> getAttenderById(@PathVariable("id") Long id) {
        return attenderService.findAttenderById(id);
    }

    @GetMapping("")
    public Mono<ResponseEntity<List<AttenderDto>>> getAttenders() {
        return attenderService.findAttenders()
                .map(attenderDtos -> ResponseEntity.ok().body(attenderDtos));
    }
}
