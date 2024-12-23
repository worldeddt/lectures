package api.lectures.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    INVALID_INSTRUCTOR_ID(HttpStatus.CONFLICT, "LC_0004", "invalid instructor id"),
    INVALID_VENUE_ID(HttpStatus.CONFLICT, "LC_0003", "invalid venue id"),
    OVER_SEAT_COUNT_REGISTERING(HttpStatus.CONFLICT, "LC_0002", "over seat registering"),
    NOT_FOUND_LECTURE(HttpStatus.CONFLICT, "LC_0001", "not found lecture");

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
