package signiel.heartsigniel.model.partymember;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.room.Room;

import javax.transaction.Transactional;

@Service
@Transactional
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;

    public PartyMemberService(PartyMemberRepository partyMemberRepository){
        this.partyMemberRepository = partyMemberRepository;
    }

    public PartyMember createPartyMember(Member member, boolean isPartyLeader){
        PartyMember partyMember = PartyMember.builder()
                .isPartyLeader(isPartyLeader)
                .isRoomLeader(false)
                .isSpecialUser(false)
                .member(member)
                .build();

        return partyMemberRepository.save(partyMember);
    }

    public void deletePartyMember(PartyMember partyMember){
        partyMemberRepository.delete(partyMember);
    }

    //성별에 맞는 파티를 세팅하는 로직
    public PartyMember assignPartyBasedOnMemberGender(Room targetRoom, PartyMember targetPartyMember){

        Party maleParty = targetRoom.getMaleParty();
        Party femaleParty = targetRoom.getFemaleParty();

        Party targetPartyEntity = (maleParty.getPartyGender() == targetPartyMember.getMember().getGender()) ? maleParty : femaleParty;

        targetPartyMember.setParty(targetPartyEntity);
        return targetPartyMember;
    }

    public void delegatePartyLeader(PartyMember partyMember){
        partyMember.setPartyLeader(true);
        partyMemberRepository.save(partyMember);
    }

    public void assignRoomLeader(PartyMember partyMember){
        partyMember.setRoomLeader(true);
        partyMemberRepository.save(partyMember);
    }

}
