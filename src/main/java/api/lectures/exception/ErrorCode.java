package api.lectures.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND_VENUE(HttpStatus.NOT_FOUND, "LC_0009", "venue not found"),
    RESOURCE_IS_LOCKED(HttpStatus.BAD_REQUEST, "LC_0009", "Resource is locked"),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "LC_0008", "application not found"),
    ATTENDER_DUPLICATED(HttpStatus.BAD_REQUEST, "LC_0007", "duplicate attender"),
    LECTURE_IS_FULL(HttpStatus.BAD_REQUEST, "LC_0006", "lecture full"),
    INVALID_ATTENDER_ID(HttpStatus.BAD_REQUEST, "LC_0005", "invalid attender id"),
    INVALID_INSTRUCTOR_ID(HttpStatus.BAD_REQUEST, "LC_0004", "invalid instructor id"),
    INVALID_VENUE_ID(HttpStatus.BAD_REQUEST, "LC_0003", "invalid venue id"),
    OVER_SEAT_COUNT_REGISTERING(HttpStatus.BAD_REQUEST, "LC_0002", "over seat registering"),
    NOT_FOUND_LECTURE(HttpStatus.NOT_FOUND, "LC_0001", "not found lecture");

    private final HttpStatus httpStatus;
    private final String code;
    private final String reason;

    public ApplicationException build() {
        return new ApplicationException(httpStatus, code, reason);
    }

    public ApplicationException build(Object ...args) {
        return new ApplicationException(httpStatus, code, reason.formatted(args));
    }
}
