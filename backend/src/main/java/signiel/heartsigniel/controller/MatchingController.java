package signiel.heartsigniel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.matching.MatchingService;

@RestController
@RequestMapping("/match")
public class MatchingController {

    private final MatchingService matchingService;
    private final AlarmService alarmService;

    public MatchingController(MatchingService matchingService, AlarmService alarmService){
        this.matchingService = matchingService;
        this.alarmService = alarmService;
    }

    @PostMapping("/members/{memberId}")
    public ResponseEntity<Response> quickMatch(@PathVariable Long memberId){
        Response response = matchingService.quickFindMatch(memberId);


            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/partymembers/{partyMemberId}")
    public ResponseEntity<Response> cancelQuickMatch(@PathVariable Long partyMemberId){
            Response response = matchingService.cancelFindMatch(partyMemberId);
            return ResponseEntity.ok(response);
    }
}
