package signiel.heartsigniel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepository;

import java.util.Optional;

@RestController
@RequestMapping("/test")
public class QuestionController {
    @Autowired
    private QuestionRepository questionRepo;

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
