package api.lectures.controller.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ResponseLectureApplicationDto {
    private Long id;
    private Long attenderId;
    private Long lectureId;
    private String status;
}
