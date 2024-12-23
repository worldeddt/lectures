package api.lectures.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("lecture")
public class Lecture extends BaseEntity {
    @Id
    @Column("lecture_id")
    private Long id;
    private String name;
    private String description;
    private Long seatCount;
    private LocalDateTime startTime;
    private Long instructorId;
    private Long venueId;
}