package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.dto.ChatMessage;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations operations;

    @MessageMapping("/room/{roomId}")
    public void send(@DestinationVariable Long roomId, @RequestBody @Valid ChatMessage chatMessage){
        operations.convertAndSend("/topic/room/"+roomId, chatMessage);
    }
}
