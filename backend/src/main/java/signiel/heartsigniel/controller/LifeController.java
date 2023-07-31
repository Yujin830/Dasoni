package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.life.LifeService;


@RequiredArgsConstructor
@RestController
@Slf4j
public class LifeController {
    private final LifeService lifeService;
    @PostMapping("/life/{memberId}")
    public ResponseEntity<String> insert(@PathVariable Long memberId) throws Exception{
        return new ResponseEntity<>(lifeService.insert(memberId), HttpStatus.OK);
    }
}
