package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.chat.dto.WhisperMessage;
import signiel.heartsigniel.model.guide.Guide;
import signiel.heartsigniel.model.guide.GuideRepo;
import signiel.heartsigniel.model.guide.dto.GuideDto;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepository;
import signiel.heartsigniel.model.room.PrivateRoomService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;
    private final GuideRepo guideRepo;
    private final QuestionRepository questionRepository;


    /**
     * 시그널 보내기 시작!
     * @param roomId
     */

    @MessageMapping("room/{roomId}/signal")
    public void startSendingSignal(@DestinationVariable Long roomId){
        String endMessage = "START SENDING SIGNAL!!!!";
        operations.convertAndSend("/topic/room/"+roomId+"/signal", endMessage);
    }
    /**
     * 질문 리스트 전송
     * @param roomId 방 번호 식별용
     */
    @MessageMapping("room/{roomId}/questions")
    public void sendQuestions(@DestinationVariable Long roomId){
        List<Question> questionList = questionRepository.randomQuestion();
        operations.convertAndSend("/topic/room/"+roomId+"/questions", questionList);
    }


    /**
     * 시작 시 가이드 메시지 출력하기 위한 메소드.
     * @param roomId : 방 번호 식별용
     * @param visibleTime : 시간, 내용 받아오기용.
     */
    @MessageMapping("room/{roomId}/guide")
    public void sendGuideMessage(@DestinationVariable Long roomId, @Payload Long visibleTime){
        String content = guideRepo.findByVisibleTime(visibleTime).get().getContent();
        operations.convertAndSend("/topic/room/"+roomId+"/guide", content);
    }


    /**
     * Chat method
     * @param roomId : 방 식별용
     * @param chatMessage : senderNickname, Content 형식
     */
    @MessageMapping("room/{roomId}/chat")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage){
        log.info("Sending Complete");
        operations.convertAndSend("/topic/room/"+ roomId +"/chat", chatMessage);
    }

    /***
     * 귓속말 보내기
     * @param roomId : 방 식별용
     * @param whisperMessage : receiverId, gender, content로 구성
     */
    @MessageMapping("room/{roomId}/whisper")
    public void whisperChatting(@DestinationVariable Long roomId, @Payload WhisperMessage whisperMessage){
        log.info("Whisper Complete");
        whisperMessage.setStatus("OK");
        operations.convertAndSendToUser(whisperMessage.getReceiverId(),"/queue/room./"+roomId + "/whisper", whisperMessage);
    }


    /**
     * 실시간 갱신을 위한 method
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}")
    public void joinAndQuitRoom(@DestinationVariable Long roomId, @Payload String msg){
        if (msg.equals("quit")) {
            privateRoomService.broadcastQuitMemberList(roomId);;
        }else if(msg.equals("join")){
            privateRoomService.broadcastJoinMemberList(roomId);
        }else{
            privateRoomService.broadcastCreateMessage(roomId);
        }
    }

    /**
     * 시작 버튼 누를 시 StartMessage를 보내서 방 전체 인원을 MeetingRoom으로 보냄.
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}/start")
    public void sendStartMessage(@DestinationVariable Long roomId){
        log.info("STARTING MESSAGE SENDING COMPLETE1!!!");
        privateRoomService.sendMessage(roomId);
        log.info("STARTING MESSAGE SENDING COMPLETE2!!!");
    }


}
