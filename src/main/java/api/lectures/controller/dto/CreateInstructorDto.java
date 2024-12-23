package api.lectures.controller.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateInstructorDto {
    private String name;
    private String phone;
}
