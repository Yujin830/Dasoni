package signiel.heartsigniel.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.MemberService;
import signiel.heartsigniel.model.member.dto.MeetingRoomMemberReq;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    //Spring의 WebSocket 기능을 사용하여 WebSocket 클라이언트와 서버 간에 메시지를 송수신하는 데 사용되는 인터페이스
    private final SimpMessageSendingOperations sendingOperations;
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    @MessageMapping("/chat/message")
    public void enter(ChatMessage messageDto){
        log.info("success");
        Member member = chatService.getLoggedInMember();

        if(ChatMessage.MessageType.ENTER.equals(messageDto.getType())){
            log.info("message");


            if(member != null) {
                messageDto.setMessage(member.getNickname() + "님이 입장하였습니다.");
            }
        }else if (ChatMessage.MessageType.LEAVE.equals(messageDto.getType())){
            messageDto.setMessage(member.getNickname()+"님이 나갔습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/room/"+messageDto.getRoomId(),messageDto);
    }



}
