package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.guide.Guide;
import signiel.heartsigniel.model.guide.GuideService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class GuideController {
    private final GuideService guideService;

    @GetMapping("/guide")
    public ResponseEntity<List<Guide>> selectAll(){
        return new ResponseEntity<>(guideService.selectAll(), HttpStatus.OK);
    }
}
