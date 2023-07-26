package signiel.heartsigniel.model.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.*;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import javax.transaction.Transactional;

@Service
@Transactional
public class PrivateRoomService {

    private final PartyRepository partyRepository;
    private final RoomRepository roomRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final MemberRepository memberRepository;

    public PrivateRoomService(RoomRepository roomRepository, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, MemberRepository memberRepository) {
        this.partyRepository = partyRepository;
        this.roomRepository = roomRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.memberRepository = memberRepository;
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
        Party femaleParty = new Party();
        femaleParty.setPartyGender("female");
        partyRepository.save(maleParty);
        partyRepository.save(femaleParty);

        // 리팩터링 필요(이해하기 쉽게 메서드로 처리)
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);

        // 파티 멤버 비즈니스 로직으로 수정
        PartyMember partyMember = new PartyMember();
        Member member = memberRepository.findById(privateRoomCreateRequest.getMemberId())
                        .orElseThrow(()-> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        partyMember.setParty(member.getGender().equals("male") ? maleParty : femaleParty);
        partyMember.setPartyLeader(true);
        partyMemberRepository.save(partyMember);

        roomRepository.save(room);
        return Response.of(CommonCode.GOOD_REQUEST, PrivateRoomCreated.builder().createdRoomId(room.getId()).build());
    }

    // 방 참가
    @Transactional
    public Response joinRoom(JoinPrivateRoomRequest joinPrivateRoomRequest) {

        MemberAndRoomOfService memberAndRoomEntity =
                findEntityById(joinPrivateRoomRequest.getUserId(), joinPrivateRoomRequest.getRoomId());

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
            throw new RuntimeException("해당 방에 더 이상 참가할 수 없습니다. 인원이 초과되었습니다.");
        }

        // 파티 참가 및 저장(향후 partyService측 비즈니스로직으로 수정)
        PartyMember partyMember = new PartyMember();
        partyMember.setMember(memberEntity);
        partyMember.setParty(party);
        partyMember.setPartyLeader(party.getMembers().isEmpty());
        partyMemberRepository.save(partyMember);

        return Response.of(RoomCode.SUCCESS_PARTICIPATE_ROOM, null);
    }

    // 사설방 조회
    public Page<PrivateRoomList> getPrivateRooms(Pageable pageable) {
        Page<PrivateRoomList> roomPage = roomRepository.findAllByType("private", pageable);
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

    public Party findPartyByGender(Room targetRoom, String gender){

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

    public MemberAndRoomOfService findEntityById(Long memberId, Long roomId){

        Member memberEntity = findMemberById(memberId);
        Room roomEntity = findRoomById(roomId);

        return MemberAndRoomOfService.builder().
                room(roomEntity).
                member(memberEntity).
                build();
    }


}