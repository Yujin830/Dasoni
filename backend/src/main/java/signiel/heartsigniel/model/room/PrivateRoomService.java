package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.party.PartyService;
import signiel.heartsigniel.model.party.exception.FullPartyException;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.partymember.PartyMemberService;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.*;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PrivateRoomService {

    private final PartyRepository partyRepository;
    private final RoomRepository roomRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final MemberRepository memberRepository;
    private final PartyService partyService;
    private final PartyMemberService partyMemberService;

    public PrivateRoomService(RoomRepository roomRepository, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, MemberRepository memberRepository, PartyService partyService, PartyMemberService partyMemberService) {
        this.partyRepository = partyRepository;
        this.roomRepository = roomRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.memberRepository = memberRepository;
        this.partyService = partyService;
        this.partyMemberService = partyMemberService;
    }

    // 방 생성
    public Response createRoom(PrivateRoomCreate privateRoomCreateRequest) {

        // 방 생성
        Room room = new Room();
        room.setRoomType("private");
        room.setRatingLimit(privateRoomCreateRequest.getRatingLimit());
        room.setMegiAcceptable(privateRoomCreateRequest.isMegiAcceptable());
        room.setTitle(privateRoomCreateRequest.getTitle());

        // 파티 생성, 파티측 비즈니스 로직으로 대체(파티 가입 로직)
        Party maleParty = partyService.createParty("private", "male");
        Party femaleParty = partyService.createParty("private", "female");

        // 리팩토링 필요(이해하기 쉽게 메서드로 처리)
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);

        // 파티 멤버 비즈니스 로직으로 수정

        Member member = findMemberById(privateRoomCreateRequest.getMemberId());
        PartyMember partyMember = partyService.joinParty(getMemberGender(member).equals("male") ? maleParty : femaleParty, member);
        partyMemberService.assignRoomLeader(partyMember);

        roomRepository.save(room);
        return Response.of(CommonCode.GOOD_REQUEST, PrivateRoomCreated.builder().createdRoomId(room.getId()).build());
    }

    // 방 참가
    public Response joinRoom(Long memberId, Long roomId) {

        MemberAndRoomOfService memberAndRoomEntity = findEntityById(memberId, roomId);

        Room roomEntity = memberAndRoomEntity.getRoom();
        Member memberEntity = memberAndRoomEntity.getMember();

        // 방 인원 초과시
        if (roomEntity.roomMemberCount() >= 6) {
            return Response.of(RoomCode.FULL_ROOM, null);
        }

        // 성별에 맞는 파티 찾기
        Party party = findPartyByGender(roomEntity, memberEntity.getGender());

        // 파티 인원 꽉 찼는지 확인하기(추후에 오류코드 수정)
        if (party.getMembers().size() >= 3) {
            throw new FullPartyException("해당 방에 더 이상 참가할 수 없습니다. 인원이 초과되었습니다.");
        }

        // 파티 참가 및 저장(향후 partyService측 비즈니스로직으로 수정)
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

        // 나 혼자 있는 경우
        if (roomEntity.roomMemberCount() == 1L) {

            partyService.quitParty(partyMemberEntity);
            roomBoom(roomEntity);

            Response response = Response.of(RoomCode.USER_OUT_FROM_ROOM, PrivateRoomInfo.of(roomEntity));
            return response;

        } else {
            // 내가 방장인 경우
            if (partyMemberEntity.isRoomLeader()){
                partyService.quitParty(partyMemberEntity);

                // 파티원이 있는 경우
                if(partyEntity.getMembers().size() >= 1){
                    System.out.println("지금 몇명이다~~~" + partyEntity.getMembers().size());
                    PartyMember newRoomLeader = partyService.findPartyLeaderByParty(partyEntity);
                    System.out.println("지금 몇명이다~~~" + partyEntity.getMembers().size());
                    partyMemberService.assignRoomLeader(newRoomLeader);
                }
                // 파티원이 없는 경우
                else {
                    System.out.println("지금 몇명이다~~~" + partyEntity.getMembers().size());
                    Party oppositeParty = findOppositePartyByGender(roomEntity, partyEntity.getPartyGender());
                    PartyMember newRoomLeader = partyService.findPartyLeaderByParty(oppositeParty);
                    partyMemberService.assignRoomLeader(newRoomLeader);
                }
            }
            // 내가 방장이 아닌 경우
            else {
                partyService.quitParty(partyMemberEntity);
            }

            Response response = Response.of(RoomCode.USER_OUT_FROM_ROOM, null);
            return response;
        }
    }

    public Response roomInfo(Long roomId){
        Room roomEntity = findRoomById(roomId);
        PrivateRoomInfo privateRoomInfo = PrivateRoomInfo.of(roomEntity);
        return Response.of(CommonCode.GOOD_REQUEST, privateRoomInfo);
    }

    // 사설방 리스트 조회
    public Page<PrivateRoomList> getPrivateRoomsByTitle(String searchKeyword, Pageable pageable) {
        Page<Room> roomList = roomRepository.findRoomByTitleContaining(searchKeyword, pageable);
        // 각 Room 엔티티를 PrivateRoomList DTO로 변환
        return roomList.map(room-> new PrivateRoomList(room));
    }

    public Page<PrivateRoomList> getPrivateRooms(Pageable pageable){
        Page<Room> roomList = roomRepository.findAllByRoomType("private", pageable);
        return roomList.map(room -> new PrivateRoomList(room));
    }

    public Page<PrivateRoomList> filterRoomByGender(String gender, Pageable pageable){
        if(gender == "male"){
            Page<Room> roomList = roomRepository.findAllByRoomTypeAndFemalePartyMemberCountLessThanEqual("private", 3L, pageable);
            return roomList.map(room -> new PrivateRoomList(room));
        } else {
            Page<Room> roomList = roomRepository.findAllByRoomTypeAndMalePartyMemberCountLessThanEqual("private", 3L, pageable);
            return roomList.map(room-> new PrivateRoomList(room));
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
    public Party findPartyByGender(Room targetRoom, String gender){

        Party party;
        if (gender.equals("female")){
            party = targetRoom.getFemaleParty();
        } else if (gender.equals("male")) {
            party = targetRoom.getMaleParty();
        } else {
            throw new RuntimeException("잘못된 성별 정보입니다." + gender);
        }

        return party;
    }

    // 방 + 유저 찾기
    public MemberAndRoomOfService findEntityById(Long memberId, Long roomId){

        Member memberEntity = findMemberById(memberId);
        Room roomEntity = findRoomById(roomId);

        return MemberAndRoomOfService.builder().
                room(roomEntity).
                member(memberEntity).
                build();
    }

    // 방에 속한 파티 찾기
    public Party findOppositePartyByGender(Room targetRoom, String gender){

        Party party;
        if (gender.equals("male")){
            party = targetRoom.getFemaleParty();
        } else if (gender.equals("female")) {
            party = targetRoom.getMaleParty();
        } else {
            throw new RuntimeException("잘못된 성별 정보입니다." + gender);
        }

        return party;
    }

    // 유저 성별 추출
    public String getMemberGender(Member member){
        return member.getGender();
    }

    public void roomBoom(Room roomEntity){
        partyRepository.delete(roomEntity.getMaleParty());
        partyRepository.delete(roomEntity.getFemaleParty());
        roomRepository.delete(roomEntity);
    }

}
