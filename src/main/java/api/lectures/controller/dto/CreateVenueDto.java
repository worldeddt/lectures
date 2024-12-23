package api.lectures.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateVenueDto {
    private String name;
    private String address;
    private Long seatCount;
}
