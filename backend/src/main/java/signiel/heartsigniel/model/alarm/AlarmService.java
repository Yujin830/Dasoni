package signiel.heartsigniel.model.alarm;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import signiel.heartsigniel.model.alarm.code.AlarmCode;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.roommember.RoomMember;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
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
            sendEmitterMessage(roomMember, room.getVideoUrl());
        }

        // TODO
        // -> 모인 6명을 meetingroom에 집어넣기.
    }

    private void sendEmitterMessage(RoomMember roomMember, String videoUrl) {
        SseEmitter emitter = this.emitters.get(roomMember.getMember().getMemberId());

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("match").data("화상채팅방 구현하면 고"));
                emitter.complete();
            } catch (IOException e) {
                // Emit a send failure error
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