package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.PrivateRoomService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

//    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;


//    @MessageMapping("/room/{roomId}")
//    public void send(@DestinationVariable Long roomId,String msg){
//        privateRoomService.renewMemberList(roomId, msg);
//        System.out.println("send arrive");
//        System.out.println("msg " + msg);
//        System.out.println("roomId " + roomId);
//
//        map.put("result", "success");
////        operations.convertAndSend("/topic/room/"+roomId, map);
//    }

    @MessageMapping("room/{roomId}")
    public void joinAndQuitRoom(@DestinationVariable Long roomId, String msg){
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
