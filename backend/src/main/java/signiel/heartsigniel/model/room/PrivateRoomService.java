package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.room.dto.JoinPrivateRoomRequest;
import signiel.heartsigniel.model.room.dto.PrivateRoomCreateRequest;

import javax.transaction.Transactional;

@Service
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

    @Transactional
    public Room createRoom(PrivateRoomCreateRequest privateRoomCreateRequest) {

        Room room = new Room();
        room.setRoomType("private");
        room.setRatingLimit(privateRoomCreateRequest.getRatingLimit());
        room.setMegiAcceptable(privateRoomCreateRequest.isMegiAcceptable());
        room.setTitle(privateRoomCreateRequest.getTitle());

        // Create new parties for the room
        Party maleParty = new Party();
        maleParty.setPartyGender("male");
        Party femaleParty = new Party();
        femaleParty.setPartyGender("female");
        partyRepository.save(maleParty);
        partyRepository.save(femaleParty);

        // Add parties to the room
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);

        // Add the user who created the room to the appropriate party
        PartyMember partyMember = new PartyMember();
        partyMember.setMember(privateRoomCreateRequest.getMember());
        partyMember.setParty(privateRoomCreateRequest.getMember().getGender().equals("male") ? maleParty : femaleParty);
        partyMember.setPartyLeader(true);
        partyMemberRepository.save(partyMember);

        roomRepository.save(room);
        return room;
    }

    @Transactional
    public void joinRoom(Member member, Room room) {

        // 방 인원 초과시
        if (room.roomMemberCount() >= 6) {
            throw new RuntimeException("해당 방에 더 이상 참가할 수 없습니다. 인원이 초과되었습니다.");
        }

        // 성별에 맞는 파티 찾기
        Party party;
        if (member.getGender().equals("male")) {
            party = room.getMaleParty();
        } else if (member.getGender().equals("female")) {
            party = room.getFemaleParty();
        } else {
            throw new RuntimeException("잘못된 성별 정보입니다.");
        }

        // 파티 인원 꽉 찼는지 확인하기
        if (party.getMembers().size() >= 3) {
            throw new RuntimeException("해당 방에 더 이상 참가할 수 없습니다. 인원이 초과되었습니다.");
        }

        // Add the member to the party
        PartyMember partyMember = new PartyMember();
        partyMember.setMember(member);
        partyMember.setParty(party);
        partyMember.setPartyLeader(party.getMembers().isEmpty());
        partyMemberRepository.save(partyMember);

    }



}