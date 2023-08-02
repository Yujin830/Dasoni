package signiel.heartsigniel.model.alarm;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import signiel.heartsigniel.model.alarm.code.AlarmCode;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.room.Room;

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
        for (PartyMember member : room.getMaleParty().getMembers()) {
            sendEmitterMessage(member, room.getVideoUrl());
        }

        // female party members
        for (PartyMember member : room.getFemaleParty().getMembers()) {
            sendEmitterMessage(member, room.getVideoUrl());
        }
    }

    private void sendEmitterMessage(PartyMember member, String videoUrl) {
        SseEmitter emitter = this.emitters.get(member.getMember().getMemberId());

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