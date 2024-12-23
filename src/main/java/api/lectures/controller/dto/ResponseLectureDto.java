package api.lectures.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;



@Getter
@Builder
public class ResponseLectureDto {
    private Long id;
    private String title;
    private String description;
    private int maxAttendees;
    private int currentAttendees = 0;
    private LocalDateTime startTime;
    private Long instructorId;
    private Long venueId;
}
