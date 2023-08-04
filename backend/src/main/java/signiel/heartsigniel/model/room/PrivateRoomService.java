package signiel.heartsigniel.model.room;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.party.PartyService;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.partymember.PartyMemberService;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.*;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PrivateRoomService {

    private final PartyRepository partyRepository;
    private final RoomRepository roomRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final MemberRepository memberRepository;
    private final PartyService partyService;
    private final PartyMemberService partyMemberService;
    private final SimpMessagingTemplate template;


    public PrivateRoomService(RoomRepository roomRepository, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, MemberRepository memberRepository, PartyService partyService, PartyMemberService partyMemberService, SimpMessagingTemplate template) {
        this.partyRepository = partyRepository;
        this.roomRepository = roomRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.memberRepository = memberRepository;
        this.partyService = partyService;
        this.partyMemberService = partyMemberService;
        this.template = template;
    }

    // 방 생성
    public Response createRoom(PrivateRoomCreate privateRoomCreateRequest) {

        Member member = findMemberById(privateRoomCreateRequest.getMemberId());

        if (isMemberInAnotherRoom(member)) {
            return Response.of(RoomCode.ALREADY_IN_OTHER_ROOM, null);
        }

        Room room = createPrivateRoomObject(privateRoomCreateRequest);

        PartyMember partyMember = partyService.joinParty(getMemberGender(member).equals("male") ? room.getMaleParty() : room.getFemaleParty(), member);
        partyMemberService.assignRoomLeader(partyMember);

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

        Party party = findPartyByGender(roomEntity, memberEntity.getGender());

        if (party.getMembers().size() >= 3) {
            return Response.of(RoomCode.FULL_ROOM, null);
        }

        if (memberEntity.getRating() < roomEntity.getRatingLimit()) {
            return Response.of(RoomCode.RATING_TOO_LOW, null);
        }

        partyService.joinParty(party, memberEntity);
        Response response = Response.of(RoomCode.SUCCESS_PARTICIPATE_ROOM, PrivateRoomInfo.of(roomEntity));

        return response;
    }

    public Response quitRoom(Long memberId, Long roomId) {
        MemberAndRoomOfService memberAndRoomEntity = findEntityById(memberId, roomId);

        Member memberEntity = memberAndRoomEntity.getMember();
        Room roomEntity = memberAndRoomEntity.getRoom();

        Party partyEntity = findPartyByGender(roomEntity, memberEntity.getGender());
        PartyMember partyMemberEntity = partyService.findPartyMemberByMemberIdAndPartyId(memberId, partyEntity.getPartyId());

        partyService.quitParty(partyMemberEntity);
        // 나 혼자 있는 경우
        if (roomEntity.roomMemberCount() == 1L) {
            deleteRoomAndParties(roomEntity);
        } else {
            // 내가 방장인 경우
            if (partyMemberEntity.isRoomLeader()) {
                // 파티원이 있는 경우
                if (partyEntity.getMembers().size() >= 1) {
                    PartyMember newRoomLeader = partyService.findPartyLeaderByParty(partyEntity);
                    partyMemberService.assignRoomLeader(newRoomLeader);
                }
                // 파티원이 없는 경우
                else {
                    Party oppositeParty = findOppositePartyByGender(roomEntity, partyEntity.getPartyGender());
                    PartyMember newRoomLeader = partyService.findPartyLeaderByParty(oppositeParty);
                    partyMemberService.assignRoomLeader(newRoomLeader);
                }
            }
        }


        Response response = Response.of(RoomCode.USER_OUT_FROM_ROOM, PrivateRoomInfo.of(roomEntity));
        return response;
    }

    /*
    실시간 갱신을 위해 방 안에 있는 멤버 정보 모두 반환 해주는 메서드.
     */
    public List<Member> getMemberInRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException(roomId + " ID를 가진 방을 찾을 수 없습니다."));

        List<Party> parties = Arrays.asList(room.getMaleParty(), room.getFemaleParty());

        List<Member> membersInRoom = new ArrayList<>();
        for (Party party : parties) {
            List<PartyMember> partyMembers = partyMemberRepository.findByParty_PartyId(party.getPartyId());
            for (PartyMember partyMember : partyMembers) {
                membersInRoom.add(partyMember.getMember());
            }
        }

        return membersInRoom;
    }
    /*
    Sending START Message Method
     */

    public void sendMessage(Long roomId){
        String startMessage = "Start";
        template.convertAndSend("/topic/room/"+roomId+"/start",startMessage);
    }

    /*
    유저 목록 브로드캐스팅
     */

    public void broadcastJoinMemberList(Long roomId){
        List<Member> membersInRoom = getMemberInRoom(roomId);
        log.info("roomid = " + roomId);
        log.info("JoinMember!!!");
        template.convertAndSend("/topic/room/" + roomId, membersInRoom);
    }
    public void broadcastQuitMemberList(Long roomId){
        List<Member> membersInRoom = getMemberInRoom(roomId);
        log.info("roomid = " + roomId);
        log.info("Quit member!!!");
        template.convertAndSend("/topic/room/" + roomId, membersInRoom);
    }

    public Response roomInfo(Long roomId) {
        Room roomEntity = findRoomById(roomId);
        PrivateRoomInfo privateRoomInfo = PrivateRoomInfo.of(roomEntity);
        return Response.of(CommonCode.GOOD_REQUEST, privateRoomInfo);
    }

    // 사설방 리스트 조회
    public Page<PrivateRoomList> getPrivateRoomsByTitle(String searchKeyword, Pageable pageable) {
        Page<Room> roomList = roomRepository.findRoomByTitleContaining(searchKeyword, pageable);
        // 각 Room 엔티티를 PrivateRoomList DTO로 변환
        return roomList.map(room -> new PrivateRoomList(room));
    }

    public Page<PrivateRoomList> getPrivateRooms(Pageable pageable) {
        Page<Room> roomList = roomRepository.findAllByRoomType("private", pageable);
        return roomList.map(room -> new PrivateRoomList(room));
    }

    public Page<PrivateRoomList> filterRoomByGender(String gender, Pageable pageable) {
        if (gender.equals("male")) {
            Page<Room> roomList = roomRepository.findAllByRoomTypeAndFemalePartyMemberCountLessThanEqual("private", 3L, pageable);
            return roomList.map(room -> PrivateRoomList.of(room));
        } else {
            Page<Room> roomList = roomRepository.findAllByRoomTypeAndMalePartyMemberCountLessThanEqual("private", 3L, pageable);
            return roomList.map(room -> PrivateRoomList.of(room));
        }
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

    // 성별에 맞는 파티 찾기
    public Party findPartyByGender(Room targetRoom, String gender) {

        Party party;
        if (gender.equals("female")) {
            party = targetRoom.getFemaleParty();
        } else if (gender.equals("male")) {
            party = targetRoom.getMaleParty();
        } else {
            throw new RuntimeException("잘못된 성별 정보입니다." + gender);
        }

        return party;
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

    // 방에 속한 파티 찾기
    public Party findOppositePartyByGender(Room targetRoom, String gender) {

        Party party;
        if (gender.equals("male")) {
            party = targetRoom.getFemaleParty();
        } else if (gender.equals("female")) {
            party = targetRoom.getMaleParty();
        } else {
            throw new RuntimeException("잘못된 성별 정보입니다." + gender);
        }

        return party;
    }

    // 유저 성별 추출
    public String getMemberGender(Member member) {
        return member.getGender();
    }

    public void deleteRoomAndParties(Room roomEntity) {
        roomRepository.delete(roomEntity);
    }

    public boolean isMemberInAnotherRoom(Member member) {
        return partyMemberRepository.findPartyMemberByMember(member).isPresent();
    }

    private Room createPrivateRoomObject(PrivateRoomCreate privateRoomCreateRequest) {
        Room room = new Room();
        room.setRoomType("private");
        room.setRatingLimit(privateRoomCreateRequest.getRatingLimit());
        room.setMegiAcceptable(privateRoomCreateRequest.isMegiAcceptable());
        room.setTitle(privateRoomCreateRequest.getTitle());

        Party maleParty = partyService.createParty("private", "male");
        Party femaleParty = partyService.createParty("private", "female");

        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);

        return room;
    }

}
