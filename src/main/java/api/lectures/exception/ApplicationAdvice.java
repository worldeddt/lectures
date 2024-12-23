package api.lectures.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ApplicationAdvice {

    @ExceptionHandler(ApplicationException.class)
    Mono<ResponseEntity<ServerExceptionResponse>> handleApplicationException(ApplicationException ex) {
        log.error("ApplicationException: Code = {}, Reason = {}", ex.getCode(), ex.getReason());
        return Mono.defer(() -> Mono.just(ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ServerExceptionResponse(ex.getCode(), ex.getReason()))));
    }

    @ExceptionHandler(Exception.class)
    Mono<ResponseEntity<ServerExceptionResponse>> handleGenericException(Exception ex) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);
        return Mono.defer(() -> Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ServerExceptionResponse(
                                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                                ex.getMessage())
                )));
    }

    public record ServerExceptionResponse(String code, String reason, String details) {
        public ServerExceptionResponse(String code, String reason) {
            this(code, reason, null);
        }
    }
}