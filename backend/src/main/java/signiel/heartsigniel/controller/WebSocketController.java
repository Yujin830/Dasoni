package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.chat.dto.MemberEntryExitDto;
import signiel.heartsigniel.model.chat.dto.WhisperMessage;
import signiel.heartsigniel.model.guide.GuideRepository;
import signiel.heartsigniel.model.meeting.SignalService;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionService;
import signiel.heartsigniel.model.room.PrivateRoomService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import signiel.heartsigniel.model.question.QuestionRepository;
@RestController
@Slf4j
public class WebSocketController {

    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;
    private final GuideRepository guideRepository;
    private final ChatService chatService;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final SignalService signalService;
    private final Map<Long, List<Question>> questionListPerRoom = new ConcurrentHashMap<>();

    public WebSocketController(SimpMessageSendingOperations operations, PrivateRoomService privateRoomService, GuideRepository guideRepository, ChatService chatService, QuestionRepository questionRepository, QuestionService questionService, SignalService signalService) {
        this.operations = operations;
        this.privateRoomService = privateRoomService;
        this.guideRepository = guideRepository;
        this.chatService = chatService;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
        this.signalService = signalService;
    }


    /**
     * 유저 정보 오픈!!
     * @param roomId
     */
    @MessageMapping("room/{roomId]/open")
    public void openMembersInformation(@DestinationVariable Long roomId){
        String openInfo = "OPEN";
        operations.convertAndSend("/topic/room/" + roomId +"/open", openInfo);
    }

    /**
     * 시그널 보내기 시작!
     *
     * @param roomId
     */

    @MessageMapping("room/{roomId}/signal")
    public void startSendingSignal(@DestinationVariable Long roomId) {
        String endMessage = "SIGNAL";
        operations.convertAndSend("/topic/room/" + roomId + "/signal", endMessage);
    }

    /**
     * 질문 리스트 전송
     *
     * @param roomId 방 번호 식별용
     */
    @MessageMapping("room/{roomId}/questions")
    public void sendQuestions(@DestinationVariable Long roomId, @Payload String numberStr) {

        try {
            List<Question> questionList = questionListPerRoom.get(roomId);
            System.out.println(questionList.toString());
            int num = Integer.parseInt(numberStr);
            String question = questionList.get(num).getContent();
            log.info(question);
            operations.convertAndSend("/topic/room/" + roomId + "/questions", question);
        } catch (NumberFormatException e) {
            log.info(e.getMessage());
        }
    }



    /**
     * 시작 시 가이드 메시지 출력하기 위한 메소드.
     *
     * @param roomId      : 방 번호 식별용
     * @param visibleTime : 시간, 내용 받아오기용.
     */
    @MessageMapping("room/{roomId}/guide")
    public void sendGuideMessage(@DestinationVariable Long roomId, @Payload Long visibleTime){
        String content = guideRepository.findByVisibleTime(visibleTime).get().getContent();
        operations.convertAndSend("/topic/room/"+roomId+"/guide", content);
    }


    /**
     * Chat method
     *
     * @param roomId      : 방 식별용
     * @param chatMessage : senderNickname, Content 형식
     */
    @MessageMapping("room/{roomId}/chat")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
        log.info("Sending Complete");
        chatService.addMessage(roomId, chatMessage);
        operations.convertAndSend("/topic/room/"+ roomId +"/chat", chatMessage);
    }

    /***
     * 귓속말 보내기
     * @param roomId : 방 식별용
     * @param whisperMessage : receiverId, gender, content로 구성
     */
        @MessageMapping("room/{roomId}/whisper/{receiveMemberId}")
            public void whisperChatting(@DestinationVariable Long roomId, @DestinationVariable Long receiveMemberId ,@Payload WhisperMessage whisperMessage) {
            whisperMessage.setStatus("OK");
            int senderMemberId = Integer.parseInt(whisperMessage.getMemberId());
            int receiverMemberId = Math.toIntExact((receiveMemberId));
            SingleSignalRequest singleSignalRequest = new SingleSignalRequest(1, senderMemberId, receiverMemberId);

            signalService.storeSignalInRedis(roomId, singleSignalRequest);
            operations.convertAndSend("/topic/room/" + roomId + "/whisper/" + receiveMemberId , whisperMessage);
        }


    /**
     * 실시간 갱신을 위한 method
     *
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}")
    public void joinAndQuitRoom(@DestinationVariable Long roomId, @Payload MemberEntryExitDto memberEntryExitDto) {
        log.info(memberEntryExitDto.toString());

        Long memberId = memberEntryExitDto.getMemberId();
        String type = memberEntryExitDto.getType();

        if (type.equals("quit")) {
            privateRoomService.broadcastQuitMemberList(roomId, memberId);
        } else {
            privateRoomService.broadcastJoinMemberList(roomId, memberId);
        }
    }

    /**
     * 시작 버튼 누를 시 StartMessage를 보내서 방 전체 인원을 MeetingRoom으로 보냄.
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}/start")
    public void sendStartMessage(@DestinationVariable Long roomId){
        List<Question> questionList = questionService.makeQuestionList(); // 랜덤 질문 리스트 생성,
        questionListPerRoom.put(roomId, questionList);
        privateRoomService.sendStartMessage(roomId);
    }

    @GetMapping("/api/room/{roomId}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable Long roomId){
        return chatService.getMessages(roomId);
    }


}
