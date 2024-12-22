package api.lectures.services.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureDto {
    private Long id;
    private String title;
    private InstructorDto instructor;
    private VenueDto venue;
}
