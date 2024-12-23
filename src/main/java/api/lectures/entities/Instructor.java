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
@Table("instructor")
public class Instructor extends BaseEntity {
    @Id
    @Column("instructor_id")
    private Long id;
    private String name;
    private String phone;
}
