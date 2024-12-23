package api.lectures.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationAdvice {

    @ExceptionHandler(ApplicationException.class)
    Mono<ResponseEntity<ServerExceptionResponse>> applicationExceptionHandler(ApplicationException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ServerExceptionResponse(ex.getCode(), ex.getReason())));
    }

    @ExceptionHandler(Exception.class)
    Mono<ResponseEntity<ServerExceptionResponse>> applicationExceptionHandler(Exception ex) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ServerExceptionResponse(
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                                ex.getMessage())
                ));
    }

    public record ServerExceptionResponse(String code, String reason) {

    }
}