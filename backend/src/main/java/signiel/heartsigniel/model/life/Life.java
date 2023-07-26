package signiel.heartsigniel.model.life;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import signiel.heartsigniel.model.member.Member;

import javax.persistence.*;
import java.util.Date;

/**
 * LifeEntity
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name="life")
public class Life {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name="member_id")
    private Long memberId;

    private Date date;
}
