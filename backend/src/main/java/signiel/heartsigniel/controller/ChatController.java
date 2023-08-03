package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.room.PrivateRoomService;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;

    @MessageMapping("/room/{roomId}")
    public void send(@DestinationVariable Long roomId, String msg){
        privateRoomService.joinRoom(1L, roomId);
        System.out.println("send arrive");
        System.out.println("msg " + msg);
        System.out.println("roomId " + roomId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", "success");
//        operations.convertAndSend("/topic/room/"+roomId, map);
    }
//    @MessageMapping("/room/{roomId}")
//    public void send(@DestinationVariable Long roomId, @RequestBody @Valid ChatMessage chatMessage){
//        System.out.println("send 받음");
//        System.out.println("roomId " + roomId);
//        operations.convertAndSend("/topic/room/"+roomId, chatMessage);
//    }
}
