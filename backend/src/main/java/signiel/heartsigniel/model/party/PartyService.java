package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.user.Member;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyService {
    private final PartyRepo partyRepo;

    public Party createNewParty(@AuthenticationPrincipal CustomUserDetails user,
                                @RequestBody PartyRequest partyRequest){
        Party party = Party.from(partyRequest);
        party.addManager(user.getUser().getMemberId());

        System.out.println("user info : "+user.getUser().getLoginId());
        System.out.println("party info : "+partyRequest.getPartyId());

        return partyRepo.save(party);
    }

}
