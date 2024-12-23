package api.lectures.services.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VenueDto {
    private Long id;
    private String name;
    private String address;
    private Long seatCount;
}
