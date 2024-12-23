package api.lectures.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class CreateLectureDto {
    private String title;
    private String description;
    private int seatCount;
    private LocalDateTime startTime;
    private Long instructorId;
    private Long venueId;
}