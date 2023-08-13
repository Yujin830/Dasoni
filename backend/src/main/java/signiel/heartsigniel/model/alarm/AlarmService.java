package signiel.heartsigniel.model.alarm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import signiel.heartsigniel.model.alarm.code.AlarmCode;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.roommember.RoomMember;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlarmService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long memberId){
        SseEmitter emitter = new SseEmitter();
        this.emitters.put(memberId, emitter);

        // timeout과 completion callback 등록
        emitter.onTimeout(() -> {
            this.emitters.remove(memberId);
            // Emit a timeout error
            emitter.completeWithError(new RuntimeException(AlarmCode.ALARM_SSE_TIMEOUT.getMessage()));
        });
        emitter.onCompletion(() -> this.emitters.remove(memberId));

        return emitter;
    }

    public void sendMatchCompleteMessage(Room room) {
        // male party members
        // roomMember- > 6명이 모여있는 list.
        for (RoomMember roomMember : room.getRoomMembers()) {
            sendEmitterMessage(roomMember, room);
        }

    }

    private void sendEmitterMessage(RoomMember roomMember, Room room) {
        SseEmitter emitter = this.emitters.get(roomMember.getMember().getMemberId());

        if (emitter != null) {
            try {
                // roomId와 메시지 정보를 함께 전송
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("status", "OK");
                responseData.put("roomId", room.getId());
                emitter.send(SseEmitter.event().name("match").data(responseData));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(new RuntimeException(AlarmCode.ALARM_SEND_FAIL.getMessage()));
            }
        }
    }

    public void removeEmitter(Long memberId) {
        if (this.emitters.containsKey(memberId)) {
            this.emitters.remove(memberId);
        } else {
            // Emit an unsubscribe error if the emitter does not exist
            throw new RuntimeException(AlarmCode.ALARM_UNSUBSCRIBED.getMessage());
        }
    }
}