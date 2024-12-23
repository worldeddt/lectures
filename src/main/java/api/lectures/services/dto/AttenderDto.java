package api.lectures.services.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AttenderDto {
    private Long id;
    private String attenderNumber;
    private String name;
    private String tel;
}
