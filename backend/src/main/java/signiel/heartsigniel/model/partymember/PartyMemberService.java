package signiel.heartsigniel.model.partymember;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.room.Room;

import javax.transaction.Transactional;

@Service
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;

    public PartyMemberService(PartyMemberRepository partyMemberRepository){
        this.partyMemberRepository = partyMemberRepository;
    }

    //성별에 맞는 파티를 세팅하는 로직
    @Transactional
    public PartyMember assignPartyBasedOnMemberGender(Room targetRoom, PartyMember targetPartyMember){

        Party maleParty = targetRoom.getMaleParty();
        Party femaleParty = targetRoom.getFemaleParty();

        Party targetPartyEntity = (maleParty.getPartyGender() == targetPartyMember.getMember().getGender()) ? maleParty : femaleParty;

        targetPartyMember.setParty(targetPartyEntity);
        return targetPartyMember;
    }
}
