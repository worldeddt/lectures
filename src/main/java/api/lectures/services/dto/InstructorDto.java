package api.lectures.services.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InstructorDto {
    private Long id;
    private String name;
    private String phone;
}
