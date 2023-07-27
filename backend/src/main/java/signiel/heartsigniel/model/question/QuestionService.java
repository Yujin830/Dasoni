package signiel.heartsigniel.model.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepo questionRepo;

    public List<Question> pickRandomQuestion(){
        return questionRepo.randomQuestion();
    }
}
