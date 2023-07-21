package signiel.heartsigniel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepo;

import java.util.Optional;

@RestController
@RequestMapping("/test")
@Tag("")
public class QuestionController {
    @Autowired
    private QuestionRepo questionRepo;

    @PostMapping("/question")
    public Question create(@RequestBody Question question){
        return questionRepo.save(question);
    }

    @GetMapping("/question/{id}")
    public String read(@PathVariable int id){
        Optional<Question> questionOptional = questionRepo.findById(id);
        questionOptional.ifPresent(System.out::println);

        return "successfully executed";
    }
}
