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
@Table("attender")
public class Attender extends BaseEntity {
    @Id
    @Column("attender_id")
    private Long id;
    @Column("attender_number")
    private String attenderNumber;
    private String name;
    private String tel;
}
