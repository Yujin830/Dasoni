package signiel.heartsigniel.model.guide;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name="guide")
public class Guide {
    @Id
    @Column(name = "`guide_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long guideId;

    private String content;

    private int visibleTime;
}
