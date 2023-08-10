package signiel.heartsigniel.model.room;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.chat.dto.ChatMessageWithMember;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.dto.ChatMessageWithRoomInfo;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.life.code.LifeCode;
import signiel.heartsigniel.model.matching.MatchingService;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.roommember.RoomMemberRepository;
import signiel.heartsigniel.model.roommember.RoomMemberService;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.code.RoomMemberCode;
import signiel.heartsigniel.model.meeting.RatingService;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.*;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;
import signiel.heartsigniel.model.roommember.dto.RoomMemberInfo;
import signiel.heartsigniel.model.roommember.exception.NotFoundRoomMemberException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PrivateRoomService {

    private final LifeService lifeService;
    private final RatingService ratingService;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberService roomMemberService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MatchingService matchingService;
    private final ChatService chatService;

    public PrivateRoomService(RoomRepository roomRepository, RoomMemberRepository roomMemberRepository, MemberRepository memberRepository, RoomMemberService roomMemberService, SimpMessageSendingOperations operations,
                              SimpMessagingTemplate simpMessagingTemplate,RatingService ratingService, LifeService lifeService, MatchingService matchingService, ChatService chatService) {

        this.roomRepository = roomRepository;
        this.matchingService = matchingService;
        this.roomMemberRepository = roomMemberRepository;
        this.memberRepository = memberRepository;
        this.roomMemberService = roomMemberService;
        this.ratingService = ratingService;
        this.lifeService = lifeService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;

    }

    // 방 생성
    public Response createRoom(PrivateRoomCreate privateRoomCreateRequest) {

        Member member = findMemberById(privateRoomCreateRequest.getMemberId());

        if (isMemberInAnotherRoom(member)) {
            return Response.of(RoomCode.ALREADY_IN_OTHER_ROOM, null);
        }
        if (matchingService.isMemberInAnyQueue(privateRoomCreateRequest.getMemberId())){
            return Response.of(MatchingCode.ALREADY_IN_MATCHING_QUEUE, null);
        }

        Room room = createPrivateRoomObject(privateRoomCreateRequest);

        RoomMember roomMember = roomMemberService.createRoomMember(member, room);
        roomMemberService.assignRoomLeader(roomMember);

        roomRepository.save(room);
        return Response.of(CommonCode.GOOD_REQUEST, PrivateRoomCreated.builder().createdRoomId(room.getId()).build());
    }

    // 방 참가
    public Response joinRoom(Long memberId, Long roomId) {

        MemberAndRoomOfService memberAndRoomEntity = findEntityById(memberId, roomId);

        Room roomEntity = memberAndRoomEntity.getRoom();
        Member memberEntity = memberAndRoomEntity.getMember();

        if (isMemberInAnotherRoom(memberEntity)) {
            return Response.of(RoomCode.ALREADY_IN_OTHER_ROOM, null);
        }

        if(!doesMemberHaveEnoughLife(memberEntity)){
            return Response.of(LifeCode.LACK_OF_LIFE, null);
        }

        if (roomEntity.getRoomMembers().size() >= 6) {
            return Response.of(RoomCode.FULL_ROOM, null);
        }

        if (validateEntryByGender(memberEntity, roomId)){
            return Response.of(RoomCode.FULL_ROOM, null);
        }

        if (memberEntity.getRating() < roomEntity.getRatingLimit()) {
            return Response.of(RoomCode.RATING_TOO_LOW, null);
        }

        if(matchingService.isMemberInAnyQueue(memberId)){
            return Response.of(MatchingCode.ALREADY_IN_MATCHING_QUEUE,  null);
        }

        RoomMember roomMember = roomMemberService.createRoomMember(memberEntity, roomEntity);
        addRoomMemberToRoom(roomEntity, roomMember);

        Response response = Response.of(RoomCode.SUCCESS_PARTICIPATE_ROOM, PrivateRoomInfo.of(roomEntity));

        return response;
    }

    public Response quitRoom(Long memberId, Long roomId) {
        MemberAndRoomOfService memberAndRoomEntity = findEntityById(memberId, roomId);

        Member memberEntity = memberAndRoomEntity.getMember();
        Room roomEntity = memberAndRoomEntity.getRoom();

        RoomMember roomMemberEntity = roomMemberService.findRoomMemberByRoomIdAndMemberId(roomId, memberId );

        // 나 혼자 있는 경우
        if (roomEntity.roomMemberCount() == 1L) {
            roomRepository.delete(roomEntity);
            chatService.deleteMessages(roomId);
            return Response.of(RoomCode.ROOM_DELETED, null);
        }
        else {
            roomMemberService.quitRoom(roomMemberEntity);
            // 내가 방장인 경우
            if (roomMemberEntity.isRoomLeader()) {
                roomMemberService.assignRoomLeader(getFirstMember(roomEntity));
                roomRepository.save(roomEntity);
            }
        }

        Response response = Response.of(RoomCode.USER_OUT_FROM_ROOM, PrivateRoomInfo.of(roomEntity));
        return response;
    }

    public Response startRoom(Long roomId, Long roomLeaderId){
        RoomMember roomLeader = findRoomMemberById(roomLeaderId);
        Room roomEntity = findRoomById(roomId);

        if(!roomLeader.isRoomLeader()){
            return Response.of(RoomMemberCode.NOT_ROOM_LEADER, null);
        }

        if(roomEntity.roomMemberCount() < 6){
            return Response.of(RoomCode.INSUFFICIENT_PARTICIPANTS, null);
        }

        useLifeAndIncreaseMeetingCount(roomEntity);
        roomEntity.setStartTime(LocalDateTime.now());
        roomRepository.save(roomEntity);

        Response response = Response.of(RoomCode.START_MEETING_SUCCESSFUL, null);
        return response;
    }

    public Response endRoom(Long roomId){

        Response response = ratingService.calculateTotalResult(roomId);
        Room roomEntity = findRoomById(roomId);
        roomRepository.delete(roomEntity);
        return response;
    }

    /*
    실시간 갱신을 위해 방 안에 있는 멤버 정보 모두 반환 해주는 메서드.
     */
    public List<Member> getMemberInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException(roomId + " ID를 가진 방을 찾을 수 없습니다."));

        List<RoomMember> roomMemberList = room.getRoomMembers();

        List<Member> membersInRoom = new ArrayList<>();

        for (RoomMember roomMember : roomMemberList) {
           membersInRoom.add(roomMember.getMember());
        }
        return membersInRoom;
    }
    /*
    Sending START Message Method
     */

    public void sendStartMessage(Long roomId){
        String startMessage = "Start";
        simpMessagingTemplate.convertAndSend("/topic/room/"+roomId+"/start",startMessage);
    }

    /*
    유저 목록 브로드캐스팅
     */

    public void broadcastJoinMemberList(Long roomId, Long memberId) {
        ChatMessageWithRoomInfo chatMessage = new ChatMessageWithRoomInfo();
        Member member = findMemberById(memberId);

        Room roomEntity = findRoomById(roomId);
        PrivateRoomInfo privateRoomInfo = PrivateRoomInfo.of(roomEntity);
        chatMessage.setPrivateRoomInfo(privateRoomInfo);

        chatMessage.setSenderNickname("시스템 메시지");
        chatMessage.setContent(member.getNickname() + "님이 입장하셨습니다.");
        chatService.addMessage(roomId, chatMessage);
        chatMessage.setPrivateRoomInfo(privateRoomInfo);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);

    }
    public void broadcastQuitMemberList(Long roomId, Long memberId){
        ChatMessageWithRoomInfo chatMessage = new ChatMessageWithRoomInfo();
        Room roomEntity = findRoomById(roomId);
        Member member = findMemberById(memberId);
        PrivateRoomInfo privateRoomInfo = PrivateRoomInfo.of(roomEntity);
        chatMessage.setPrivateRoomInfo(privateRoomInfo);
        chatMessage.setContent(member.getNickname() + "님이 퇴장하셨습니다.");
        chatMessage.setSenderNickname("시스템 메시지");
        chatService.addMessage(roomId, chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage);
    }



    public Response roomInfo(Long roomId) {
        Room roomEntity = findRoomById(roomId);
        PrivateRoomInfo privateRoomInfo = PrivateRoomInfo.of(roomEntity);
        return Response.of(CommonCode.GOOD_REQUEST, privateRoomInfo);
    }

    // 사설방 리스트 조회
    public Page<PrivateRoomList> getPrivateRoomsByTitle(String searchKeyword, Pageable pageable) {
        Page<Room> roomList = roomRepository.findRoomByTitleContainingAndStartTimeIsNull(searchKeyword, pageable);
        // 각 Room 엔티티를 PrivateRoomList DTO로 변환
        return roomList.map(room -> new PrivateRoomList(room));
    }

    public Page<PrivateRoomList> getPrivateRooms(Pageable pageable){
        Page<Room> roomList = roomRepository.findAllByRoomTypeAndStartTimeIsNull("private", pageable);
        return roomList.map(room -> PrivateRoomList.of(room));
    }

    public Page<PrivateRoomList> filterRoomByGender(String gender, Pageable pageable) {
        Page<Room> roomList = roomRepository.findRoomsByGenderAndCountLessThanEqual(gender, 2L, pageable);
        return roomList.map(room -> PrivateRoomList.of(room));
    }


    // 유저 엔티티 조회
    public Member findMemberById(Long targetMemberId) {

        return memberRepository.findById(targetMemberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));
    }

    // 룸 엔티티 조회
    public Room findRoomById(Long targetRoomId) {

        return roomRepository.findById(targetRoomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
    }

    public RoomMember findRoomMemberById(Long roomMemberId){
        return roomMemberRepository.findById(roomMemberId)
                .orElseThrow(() -> new NotFoundRoomMemberException("해당 유저를 찾을 수 없습니다."));
    }

    // 방 + 유저 찾기
    public MemberAndRoomOfService findEntityById(Long memberId, Long roomId) {

        Member memberEntity = findMemberById(memberId);
        Room roomEntity = findRoomById(roomId);

        return MemberAndRoomOfService.builder().
                room(roomEntity).
                member(memberEntity).
                build();
    }

    public RoomMember getFirstMember(Room room){
        return room.getRoomMembers().get(1);
    }

    // 해당 성별 입장 가능 판단
    public boolean validateEntryByGender(Member memberEntity, Long roomId) {
        return roomMemberRepository.findRoomMembersByRoom_IdAndMember_Gender(roomId, memberEntity.getGender()).size() >= 3;
    }

    public boolean isMemberInAnotherRoom(Member member) {
        return roomMemberRepository.findRoomMemberByMember(member).isPresent();
    }

    private Room createPrivateRoomObject(PrivateRoomCreate privateRoomCreate) {
        Room room = new Room();
        room.setRoomType("private");
        room.setRatingLimit(privateRoomCreate.getRatingLimit());
        room.setMegiAcceptable(privateRoomCreate.isMegiAcceptable());
        room.setTitle(privateRoomCreate.getTitle());

        return room;
    }

    public void useLifeAndIncreaseMeetingCount(Room room){
        List<RoomMember> roomMemberList = room.getRoomMembers();
        for (RoomMember roomMember : roomMemberList){
            Member member = roomMember.getMember();
            member.setMeetingCount(member.getMeetingCount() + 1);
            memberRepository.save(member);
            lifeService.useLife(member);
        }
    }

    public boolean doesMemberHaveEnoughLife(Member member){
        return lifeService.countRemainingLives(member.getMemberId()) > 0;
    }

    public void addRoomMemberToRoom(Room room, RoomMember roomMember){
        room.getRoomMembers().add(roomMember);
        roomRepository.save(room);
    }


}
