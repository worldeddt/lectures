package api.lectures.controller;


import api.lectures.entities.Attender;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class AttenderController {


    public Mono<Attender> createAttender(Attender attender) {

    }
}
