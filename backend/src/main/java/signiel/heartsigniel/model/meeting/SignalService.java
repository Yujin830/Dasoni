package signiel.heartsigniel.model.meeting;

import org.springframework.boot.web.server.Cookie;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.meeting.code.SignalCode;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;

import java.util.List;

@Service
public class SignalService {

    private final RedisTemplate<String, SingleSignalRequest> redisTemplate;

    public SignalService(RedisTemplate<String, SingleSignalRequest> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    // 클라이언트에서 들어온 시그널 정보 캐싱
    public Response storeSignalInRedis(Long roomId, SingleSignalRequest singleSignalRequest){
        redisTemplate.opsForList().rightPush("room:" + roomId + ":signal", singleSignalRequest);
        return Response.of(SignalCode.SIGNAL_TRANSMISSION_SUCCESS, null);
    }

    // 해당 방의 모든 시그널 조회
    public List<SingleSignalRequest> fetchAllSignalsForRoom(Long roomId){
        return redisTemplate.opsForList().range("room:" + roomId + "signal", 0, -1);
    }

    // 해당 방의 모든 시그널 삭제
    public void deleteAllSignalsForRoom(Long roomId){
        redisTemplate.delete("room:" + roomId + ":signal");
    }

}
