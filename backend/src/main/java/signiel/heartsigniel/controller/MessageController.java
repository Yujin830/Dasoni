package signiel.heartsigniel.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.dto.ChatMessageDto;

@RestController
@RequiredArgsConstructor
public class MessageController {

    //Spring의 WebSocket 기능을 사용하여 WebSocket 클라이언트와 서버 간에 메시지를 송수신하는 데 사용되는 인터페이스
    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void enter(ChatMessageDto messageDto){
        if(ChatMessageDto.MessageType.ENTER.equals(messageDto.getType())){
            messageDto.setMessage(messageDto.getSender()+"님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/chatroom"+messageDto.getRoomId(),messageDto);
    }



}
