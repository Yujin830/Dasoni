package signiel.heartsigniel.model.partyMember;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyMemberService {
    private final PartyMemberRepo partyMemberRepo;
    private final PartyRepo partyRepo;

    public void joinToParty(@AuthenticationPrincipal CustomUserDetails user, Long partyId){
        System.out.println("PartyMemberService : joinToParty");

        List<PartyMember> partyMembers = partyMemberRepo.findAllByPartyId(partyId);


        // 메기 파티장 임의로 false로 지정 (수정 필요)
        PartyMember partyMember = new PartyMember(partyId, user.getMember().getMemberId(), false, false);

        int beforeJoin = partyMembers.size();

        partyMemberRepo.save(partyMember);

        Party party = partyRepo.findByPartyId(partyId);

        int changeRating = (( party.getAvgRating() * beforeJoin ) + user.getMember().getRank() ) / (beforeJoin + 1);

        party.setAvgRating(changeRating);
        partyRepo.save(party);

    }
}
