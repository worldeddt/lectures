package api.lectures.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "venue")
public class Venue extends BaseEntity {
    @Id
    @Column("venue_id")
    private Long id;
    private String name;
    private String address;
    private Long seatCount;
}
