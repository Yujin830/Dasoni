package signiel.heartsigniel.model.chat;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.chat.dto.ChatMessage;

import java.util.List;

@Service
public class ChatService {
    private final RedisTemplate<String, ChatMessage> redisTemplate;

    public ChatService(RedisTemplate<String, ChatMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 메시지를 추가하는 메서드
    public void addMessage(Long roomId, ChatMessage message) {
        redisTemplate.opsForList().rightPush("room:" + roomId + "messages", message);
    }

    // 특정 방의 모든 메시지를 가져오는 메서드
    public List<ChatMessage> getMessages(Long roomId) {
        return redisTemplate.opsForList().range("room:" + roomId + "messages", 0, -1);
    }
}
