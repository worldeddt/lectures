package api.lectures.controller.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreateLectureDto {
    private String title;
    private String description;
    private int seatCount;
    private String startTime;
    private Long instructorId;
    private Long venueId;
}
