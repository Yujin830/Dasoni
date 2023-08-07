package signiel.heartsigniel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.matching.MatchingService;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.matching.dto.QueueData;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.rating.dto.TotalResultRequest;

@RestController
@RequestMapping("/api/match")
public class MatchingController {

    private final MatchingService matchingService;
    private final AlarmService alarmService;

    public MatchingController(MatchingService matchingService, AlarmService alarmService){
        this.matchingService = matchingService;
        this.alarmService = alarmService;
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Response> cancelQuickMatch(@RequestBody QueueData queueData){
            Response response = matchingService.dequeueMember(queueData);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/members/{memberId}")
    public ResponseEntity<Response> findQuickMatch(@PathVariable Long memberId) {
        Response response = matchingService.enqueueMember(memberId);
        return ResponseEntity.ok(response);
    }

}
