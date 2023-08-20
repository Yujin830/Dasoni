package signiel.heartsigniel.model.warn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name="warn")
public class Warn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    @Column(name="warn_date")
    private LocalDate warnDate;
}
