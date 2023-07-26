package signiel.heartsigniel.model.question;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        return questions.subList(0, Math.min(count, questions.size()));
    }
}
