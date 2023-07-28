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


    public PrivateRoomService(RoomRepository roomRepository, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, MemberRepository memberRepository, PartyService partyService) {
        this.partyRepository = partyRepository;
        this.roomRepository = roomRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.memberRepository = memberRepository;
        this.partyService = partyService;
    }

    // 방 생성
    public Response createRoom(PrivateRoomCreate privateRoomCreateRequest) {

        Room room = new Room();
        room.setRoomType("private");
        room.setRatingLimit(privateRoomCreateRequest.getRatingLimit());
        room.setMegiAcceptable(privateRoomCreateRequest.isMegiAcceptable());
        room.setTitle(privateRoomCreateRequest.getTitle());

        // 파티측 비즈니스 로직으로 대체
        Party maleParty = new Party();
        maleParty.setPartyGender("male");
        maleParty.setPartyType("private");
        Party femaleParty = new Party();
        femaleParty.setPartyGender("female");
        femaleParty.setPartyType("private");
        partyRepository.save(maleParty);
        partyRepository.save(femaleParty);

        // 리팩터링 필요(이해하기 쉽게 메서드로 처리)
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);

        // 파티 멤버 비즈니스 로직으로 수정
        PartyMember partyMember = new PartyMember();
        Member member = findMemberById(privateRoomCreateRequest.getMemberId());
        partyMember.setParty(member.getGender().equals("male") ? maleParty : femaleParty);
        partyMember.setMember(member);
        partyMember.setPartyLeader(true);
        partyMemberRepository.save(partyMember);
        roomRepository.save(room);
        System.out.println(room.getId());
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
        PartyMember partyMember = new PartyMember();
        partyMember.setMember(memberEntity);
        partyMember.setSpecialUser(false);
        partyMember.setParty(party);
        partyMember.setPartyLeader(party.getMembers().isEmpty());
        partyMemberRepository.save(partyMember);

        return Response.of(RoomCode.SUCCESS_PARTICIPATE_ROOM, null);
    }

    public PrivateRoomInfo roomInfo(Long roomId){
        Room roomEntity = findRoomById(roomId);
        return new PrivateRoomInfo(roomEntity);
    }

    public Response quitRoom(Long memberId, Long roomId) {
        MemberAndRoomOfService memberAndRoomEntity = findEntityById(memberId, roomId);

        Member memberEntity = memberAndRoomEntity.getMember();
        Room roomEntity = memberAndRoomEntity.getRoom();

        Party partyEntity = findPartyByGender(roomEntity, memberEntity.getGender());
        PartyMember partyMemberEntity = partyService.findPartyMemberByMemberIdAndPartyId(memberId, partyEntity.getPartyId());


        // 향후 로직 각 도메인별 서비스 계층 로직으로 수정
        if (roomEntity.roomMemberCount() == 1L) {

            partyMemberRepository.delete(partyMemberEntity);
            if (partyEntity.getPartyGender() == "male"){
                partyRepository.delete(roomEntity.getFemaleParty());
                partyRepository.delete(partyEntity);
            } else {
                partyRepository.delete(roomEntity.getMaleParty());
                partyRepository.delete(partyEntity);
            }
            roomRepository.delete(roomEntity);

            return Response.of(RoomCode.USER_OUT_FROM_ROOM, null);

        } else {
            //파티원이 남아있는 경우
            if (partyEntity.getMembers().size() >= 2) {
                // 내가 파티장인 경우
                if (memberEntity == partyService.findPartyLeaderByParty(partyEntity)) {
                    partyMemberRepository.delete(partyMemberEntity);
                    List<PartyMember> partyMembers = partyEntity.getMembers();
                    PartyMember newLeader = partyMembers.get(0);
                    newLeader.setPartyLeader(true);

                    return Response.of(RoomCode.USER_OUT_FROM_ROOM, null);
                }

                partyMemberRepository.delete(partyMemberEntity);
                return Response.of(RoomCode.USER_OUT_FROM_ROOM, null);
            }

            return Response.of(RoomCode.NOT_PARTICIPATE_ROOM, null);
        }

        }


    // 사설방 리스트 조회
    public Page<PrivateRoomList> getPrivateRooms(Pageable pageable) {
        Page roomPage = roomRepository.findAllByRoomType("private", pageable);
        // 각 Room 엔티티를 PrivateRoomList DTO로 변환
        return roomPage;
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

    // 방에 속한 파티 찾기
    public PartyByRoomService findPartyByRoom(Room targetRoom) {
        Party maleParty = targetRoom.getMaleParty();
        Party femaleParty = targetRoom.getFemaleParty();

        return PartyByRoomService.builder()
                .maleParty(maleParty)
                .femaleParty(femaleParty)
                .build();
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

}