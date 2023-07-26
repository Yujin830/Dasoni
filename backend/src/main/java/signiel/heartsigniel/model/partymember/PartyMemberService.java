package signiel.heartsigniel.model.partymember;

import org.springframework.stereotype.Service;

@Service
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;
    private final MatchingPartyService matchingPartyService;
    private final MatchingRoomService matchingRoomService;


    public PartyMemberService(PartyMemberRepository partyMemberRepository, MatchingPartyService matchingPartyService, MatchingRoomService matchingRoomService) {
        this.matchingPartyService = matchingPartyService;
        this.partyMemberRepository = partyMemberRepository;
        this.matchingRoomService = matchingRoomService;
    }
    public void leaveParty(PartyMember partyMember) {
        Party party = partyMember.getParty();

        if (partyMember.isPartyLeader()){
            // 파티 타입 따라 폭파여부 결정
            if()
        }
    }

    public void joinParty() {

    }


}
