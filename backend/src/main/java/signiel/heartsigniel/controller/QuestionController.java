package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/question")
    public ResponseEntity<List<Question>> pickRandomQuestion(){
        return new ResponseEntity<>(questionService.pickRandomQuestion(), HttpStatus.OK);
    }
}
