package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import signiel.heartsigniel.model.alarm.AlarmService;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService){
        this.alarmService = alarmService;
    }

    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        log.info("call subscribeMethod!!!");
        return alarmService.createEmitter(memberId);
    }

    @PostMapping("/unsubscribe/{memberId}")
    public void unsubscribe(@PathVariable Long memberId) throws IOException {
        alarmService.removeEmitter(memberId);
    }
}