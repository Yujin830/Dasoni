package signiel.heartsigniel.model.question;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@Data
@Entity
@Table(name="question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long question_id;

    @Column
    private String content;
}
