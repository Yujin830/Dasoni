package signiel.heartsigniel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.guide.GuideRepository;

@RestController
@Slf4j
@RequestMapping("/api/test")
public class TestController {

    private final GuideRepository guideRepository;

    public TestController(GuideRepository guideRepository) {
        this.guideRepository = guideRepository;
    }

    @GetMapping("/guide")
    public void test(){
        String content = guideRepository.findByVisibleTime(0L).get().getContent();

        log.info(content);
    }
}