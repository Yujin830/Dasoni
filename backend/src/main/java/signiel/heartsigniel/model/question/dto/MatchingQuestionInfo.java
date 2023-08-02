package signiel.heartsigniel.model.question.dto;

import lombok.Getter;
import signiel.heartsigniel.model.question.Question;

@Getter
public class MatchingQuestionInfo {

    private Long questionId;
    private String content;

    public MatchingQuestionInfo(Question question){
        this.questionId = question.getQuestion_id();
        this.content = question.getContent();
    }
}
