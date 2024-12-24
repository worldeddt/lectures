package api.lectures.controller.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyLectureDto {
    private Long attenderId;
    private Long lectureId;
}
