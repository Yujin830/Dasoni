package signiel.heartsigniel.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import signiel.heartsigniel.model.alarm.AlarmService;

import java.io.IOException;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService){
        this.alarmService = alarmService;
    }

    @GetMapping("/subscribe/{memberId}")
    public SseEmitter subscribe(@PathVariable Long memberId) {
        return alarmService.createEmitter(memberId);
    }

    @PostMapping("/unsubscribe/{memberId}")
    public void unsubscribe(@PathVariable Long memberId) throws IOException {
        alarmService.removeEmitter(memberId);
    }
}