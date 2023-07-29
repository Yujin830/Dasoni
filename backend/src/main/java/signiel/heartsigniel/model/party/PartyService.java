package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import signiel.heartsigniel.jwt.CustomUserDetails;

import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.party.exception.NoPartyLeaderException;
import signiel.heartsigniel.model.party.exception.NoPartyMemberException;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.room.Room;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;

    public Party createNewParty(@AuthenticationPrincipal CustomUserDetails user,
                                @RequestBody PartyRequest partyRequest){
        Party party = Party.from(partyRequest);
        party.addManager(user.getMember().getMemberId());


//        System.out.println("user info : "+user.getUser().getLoginId());

        System.out.println("party info : "+partyRequest.getPartyId());

        return partyRepository.save(party);
    }

    public PartyMember findPartyMemberByMemberIdAndPartyId(Long memberId, Long partyId) {
        return partyMemberRepository.findByMember_MemberIdAndParty_PartyId(memberId, partyId)
                .orElseThrow(() -> new NoPartyMemberException("파티멤버가 존재하지 않습니다."));
    }

    public Member findPartyLeaderByParty(Party targetParty){
        List<PartyMember> partyMembers = targetParty.getMembers();

        for (PartyMember partyMember : partyMembers) {
            if(partyMember.isPartyLeader()){
                return partyMember.getMember();
            }
        }

        throw new NoPartyLeaderException("파티장이 없는 파티입니다.");
    }

    // 파티의 레이팅을 계산
    public void CalculatePartyRating(Party party){
        party.calculateAndSetAvgRating();
        partyRepository.save(party);
    }

}
