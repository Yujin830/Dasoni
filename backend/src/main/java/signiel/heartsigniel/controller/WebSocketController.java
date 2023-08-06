package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.room.PrivateRoomService;

import java.util.HashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;


    @MessageMapping("room/{roomId}/chat")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage){
        operations.convertAndSend("/topic/room/"+ roomId +"/chat", chatMessage);
    }

    @MessageMapping("room/{roomId}")
    public void joinAndQuitRoom(@DestinationVariable Long roomId, @Payload String msg){
        if (msg.equals("quit")) {
            privateRoomService.broadcastQuitMemberList(roomId);;
        }else{
            privateRoomService.broadcastJoinMemberList(roomId);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("result","sucess");
    }

    @MessageMapping("room/{roomId}/start")
    public void sendStartMessage(@DestinationVariable Long roomId){
        log.info("STARTING MESSAGE SENDING COMPLETE1!!!");
        privateRoomService.sendMessage(roomId);
        log.info("STARTING MESSAGE SENDING COMPLETE2!!!");
    }


}
