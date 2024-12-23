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
@Table("lecture_application")
public class LectureApplication extends BaseEntity {
    @Id
    @Column("lecture_application_id")
    private Long id;
    @Column("attender_id")
    private Long attenderId;
    @Column("attender_id")
    private Long lectureId;
    private String status;
}
