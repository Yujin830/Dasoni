package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.matching.MatchingService;
import signiel.heartsigniel.model.member.Member;

@RestController
@RequestMapping("/matching")
@Slf4j
public class MatchingController {

    private final MatchingService matchingService;


    public MatchingController(MatchingService matchingService){
        this.matchingService = matchingService;
    }

    @PostMapping("/member/{memberId}")
    public ResponseEntity<Response> addUserToQueue(@PathVariable Long memberId){
        Response response = matchingService.addUserToQueue(memberId);
        return ResponseEntity.ok(response);
    }

}
