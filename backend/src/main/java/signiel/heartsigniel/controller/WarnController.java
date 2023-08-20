package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.warn.WarnService;

@RequiredArgsConstructor
@RestController
@Slf4j
public class WarnController {
    private final WarnService warnService;

    @PostMapping("/api/warn/{memberId}")
    public ResponseEntity<Integer> insertAndSelect(@PathVariable Long memberId) throws Exception{
        return new ResponseEntity<>(warnService.insertAndSelect(memberId), HttpStatus.OK);
    }
}
