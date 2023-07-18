package signiel.heartsigniel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepo;

import java.util.Optional;

@Tag(name="TEST", description = "swagger Test")
@RestController
@RequestMapping("/test")
public class QuestionController {
    @Autowired
    private QuestionRepo questionRepo;

    @PostMapping("/question")
    public Question create(@RequestBody Question question){
        return questionRepo.save(question);
    }

    @Operation(summary = "Question 조회", description = "Question을 조회합니다.")
    @GetMapping("/question/{id}")
    public String read(@PathVariable int id){
        Optional<Question> questionOptional = questionRepo.findById(id);
        questionOptional.ifPresent(System.out::println);

        return "successfully executed";
    }
}
