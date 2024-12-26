package api.lectures.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyLectureDto {
    private Long attenderId;
    private Long lectureId;
}
