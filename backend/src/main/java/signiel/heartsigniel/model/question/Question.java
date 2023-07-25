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
    private int question_id;

    private String content;
}
