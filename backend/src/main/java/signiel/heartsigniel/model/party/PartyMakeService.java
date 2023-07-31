package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.partyMember.PartyMemberRepo;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyMakeService {
    private final PartyRepo partyRepo;
    private final PartyMemberRepo partyMemberRepo;
    public Party createNewParty(@AuthenticationPrincipal CustomUserDetails user){
        System.out.println("PartyMakeService createNewParty");

        PartyRequest partyRequest = null;
        Member member = user.getMember();
        
        partyRequest.setPartyGender(member.getGender());
        partyRequest.setAvgRating(member.getRank());
        partyRequest.setPartyType("미정");
        
        Party party = Party.from(partyRequest);

        System.out.println("party info : "+party.toString());

        return partyRepo.save(party);
    }
}
