package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import signiel.heartsigniel.jwt.CustomUserDetails;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyService {

    private final PartyRepository partyRepository;

    public Party createNewParty(@AuthenticationPrincipal CustomUserDetails user,
                                @RequestBody PartyRequest partyRequest){
        Party party = Party.from(partyRequest);
        party.addManager(user.getMember().getMemberId());

        System.out.println("user info : "+user.getMember().getLoginId());
        System.out.println("party info : "+partyRequest.getPartyId());

        return partyRepository.save(party);
    }

}
