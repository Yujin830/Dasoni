package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.chat.dto.MatchMemberMessage;
import signiel.heartsigniel.model.chat.dto.MemberEntryExitDto;
import signiel.heartsigniel.model.chat.dto.WhisperMessage;
import signiel.heartsigniel.model.guide.GuideRepository;
import signiel.heartsigniel.model.meeting.SignalService;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionService;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.room.PrivateRoomService;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.RoomRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
public class WebSocketController {

    private final SimpMessageSendingOperations operations;
    private final PrivateRoomService privateRoomService;
    private final GuideRepository guideRepository;
    private final ChatService chatService;
    private final QuestionService questionService;
    private final SignalService signalService;
    private final MatchingRoomService matchingRoomService;
    private final RoomRepository roomRepository;

    private final Map<Long, List<Question>> questionListPerRoom = new ConcurrentHashMap<>();

    public WebSocketController(SimpMessageSendingOperations operations, PrivateRoomService privateRoomService, GuideRepository guideRepository, ChatService chatService, QuestionService questionService, SignalService signalService, MatchingRoomService matchingRoomService, RoomRepository roomRepository) {
        this.operations = operations;
        this.privateRoomService = privateRoomService;
        this.guideRepository = guideRepository;
        this.chatService = chatService;
        this.questionService = questionService;
        this.signalService = signalService;
        this.matchingRoomService = matchingRoomService;
        this.roomRepository = roomRepository;
    }



    @MessageMapping("room/{roomId}/makeQuestion")
    public void makeQuestion(@DestinationVariable Long roomId){
        List<Question> questionList = questionService.makeQuestionList(); // 랜덤 질문 리스트 생성,
        questionListPerRoom.put(roomId, questionList);
        operations.convertAndSend("/topic/room/" + roomId + "/makeQuestion", "makeQuestionComplete");
    }
    @MessageMapping("room/{roomId}/megiEnterMessage")
    public void megiEnterMessage(@DestinationVariable Long roomId){
        String msg = "메기입장";
        log.info(msg);
        operations.convertAndSend("/topic/room/"+roomId+"/megiEnterMessage", msg);
    }

    /**
     * 메기 입장 가능!
     */

    @MessageMapping("room/{roomId}/megi")
    public void canJoinMegi(@DestinationVariable Long roomId, String msg) {
        log.info("inQueueRoom1!!");

        String megi = msg;
        log.info(msg);
        matchingRoomService.enqueueRoom(roomId);
        log.info("inQueueRoom2!!");

        operations.convertAndSend("/topic/room/" + roomId + "/megi", megi);
    }

    @MessageMapping("room/{roomId}/endMegi")
    public void endMegiEnterTime(@DestinationVariable Long roomId){
        log.info("메기 입장 불가");

        matchingRoomService.dequeueRoom(roomId);

        operations.convertAndSend("/topic/room/" + roomId + "/endMegi");
    }


    /**
     * 유저 정보 오픈!!
     *
     * @param roomId
     */
    @MessageMapping("room/{roomId}/open")
    public void openMembersInformation(@DestinationVariable Long roomId) {
        String openInfo = "OPEN";
        log.info("open");
        operations.convertAndSend("/topic/room/" + roomId + "/open", openInfo);
    }


    /**
     * 최종 개인 결과 요청 오픈
     *
     * @param roomId
     */
    @MessageMapping("room/{roomId}/requestResult")
    public void openMembersResultRequest(@DestinationVariable Long roomId) {
        String requestInfo = "requestResult";
        operations.convertAndSend("/topic/room/" + roomId + "/requestResult", requestInfo);
    }

    /**
     * 서브 세션방 종료
     *
     * @param roomId
     */
    @MessageMapping("room/{roomId}/subClose")
    public void closeSubSession(@DestinationVariable Long roomId) {
        String closeInfo = "close";
        operations.convertAndSend("/topic/room/" + roomId + "/subClose", closeInfo);
    }

    /**
     * 첫 인상 투표 시작 메시지 전송.
     *
     * @param roomId
     */
    @MessageMapping("room/{roomId}/firstSignal")
    public void sendFirstSignal(@DestinationVariable Long roomId) {
        String firstMessage = "FIRST";
        operations.convertAndSend("/topic/room/" + roomId + "/firstSignal", firstMessage);
    }

    /**
     * 최종 시그널 보내기 시작!
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
            int num = Integer.parseInt(numberStr);
            String question = questionList.get(num).getContent();
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
    public void sendGuideMessage(@DestinationVariable Long roomId, @Payload Long visibleTime) {
        String content = guideRepository.findByVisibleTime(visibleTime).get().getContent();
        operations.convertAndSend("/topic/room/" + roomId + "/guide", content);
    }


    /**
     * Chat method
     *
     * @param roomId      : 방 식별용
     * @param chatMessage : senderNickname, Content 형식
     */
    @MessageMapping("room/{roomId}/chat")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
        chatService.addMessage(roomId, chatMessage);
        operations.convertAndSend("/topic/room/" + roomId + "/chat", chatMessage);
    }

    /***
     * 귓속말 보내기
     * @param roomId : 방 식별용
     * @param whisperMessage : receiverId, gender, content로 구성
     */
    @MessageMapping("room/{roomId}/whisper/{receiveMemberId}")
    public void whisperChatting(@DestinationVariable Long roomId, @DestinationVariable Long receiveMemberId, @Payload WhisperMessage whisperMessage) {
        whisperMessage.setStatus("OK");
        int sequence = 1;
        int senderId = Integer.parseInt(whisperMessage.getMemberId());
        int receiverId = Math.toIntExact(receiveMemberId);

        SingleSignalRequest signalRequest = new SingleSignalRequest(sequence, senderId, receiverId);
        signalService.storeSignalInRedis(roomId, signalRequest);

        operations.convertAndSend("/topic/room/" + roomId + "/whisper/" + receiveMemberId, whisperMessage);
    }


    /**
     * 실시간 갱신을 위한 method
     *
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}")
    public void joinAndQuitRoom(@DestinationVariable Long roomId, @Payload MemberEntryExitDto memberEntryExitDto) {

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
     *
     * @param roomId : 방 식별용
     */
    @MessageMapping("room/{roomId}/start")
    public void sendStartMessage(@DestinationVariable Long roomId) {
        privateRoomService.sendStartMessage(roomId);
    }

    @GetMapping("/api/room/{roomId}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable Long roomId) {
        return chatService.getMessages(roomId);
    }


    /***
     * 매칭된 상대와 채팅
     * @param MatchMemberMessage : receiverId, gender, content로 구성
     */
    @MessageMapping("mypage/chat/{chattingMemberId}")
    public void mathchChatting(@DestinationVariable Long chattingMemberId, @Payload MatchMemberMessage matchMemberMessage) {
        operations.convertAndSend("/queue/mypage/chat/" + chattingMemberId + "/" + matchMemberMessage.getMemberId(), matchMemberMessage);
        operations.convertAndSend("/queue/mypage/chat/" + matchMemberMessage.getMemberId() + "/" + chattingMemberId, matchMemberMessage);
    }
}
