package signiel.heartsigniel.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import signiel.heartsigniel.model.chat.dto.ChatMessage;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @MessageMapping("/message")
    public void sendMessageToRoom(Long roomId, ChatMessage message){
        template.convertAndSend("/topic/rooms/" + roomId, message);
    }
}
