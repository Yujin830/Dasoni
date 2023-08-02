package signiel.heartsigniel.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import signiel.heartsigniel.model.chat.dto.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/room/{roomId}/chat")
    @SendTo("/topic/room/{roomId}")
    public ChatMessage send(@DestinationVariable Long roomId, ChatMessage chatMessage){
        return chatMessage;
    }
}
