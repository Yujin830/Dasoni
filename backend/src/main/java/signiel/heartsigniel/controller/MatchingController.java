package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.matching.MatchingService;
import signiel.heartsigniel.model.room.MatchingRoomService;

@RestController
@Slf4j
@RequestMapping("/api/match")
public class MatchingController {

    private final MatchingService matchingService;
    private final AlarmService alarmService;
    private final MatchingRoomService matchingRoomService;

    public MatchingController(MatchingService matchingService, AlarmService alarmService, MatchingRoomService matchingRoomService){
        this.matchingService = matchingService;
        this.alarmService = alarmService;
        this.matchingRoomService = matchingRoomService;
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Response> cancelQuickMatch(@PathVariable Long memberId){
            Response response = matchingService.dequeueMember(memberId);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/members/{memberId}/{queueType}")
    public ResponseEntity<Response> findQuickMatch(@PathVariable Long memberId, @PathVariable String queueType) {

        log.info("queuetype = " + queueType);
        Response response = matchingService.enqueueMember(memberId, queueType);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rooms/{roomId}")
    public ResponseEntity<Response> enqueueRoomToRedis(@PathVariable Long roomId){
        Response response = matchingRoomService.enqueueRoom(roomId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Response> dequeueRoomToRedis(@PathVariable Long roomId){
        Response response = matchingRoomService.dequeueRoom(roomId);
        return ResponseEntity.ok(response);
    }



}
