package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.partyMember.PartyMember;
import signiel.heartsigniel.model.partyMember.PartyMemberRepo;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyService {

    private final PartyRepo partyRepo;
    private final PartyMemberRepo partyMemberRepo;
//    public Party createNewParty(@AuthenticationPrincipal CustomUserDetails user,
//                                @RequestBody PartyRequest partyRequest){
//        Party party = Party.from(partyRequest);
//        party.addManager(user.getMember().getMemberId());
//
//        System.out.println("user info : "+user.getMember().getMemberId());
//        System.out.println("party info : "+partyRequest.getPartyId());
//
//        return partyRepo.save(party);
//    }

    // 현재 유저의 랭크와 성별에 맞게 파티 찾고 파티 정원 확인
    public List<Long> findParties(@AuthenticationPrincipal CustomUserDetails user){
        System.out.println("PartyService : findParties activated");
        List<Long> partyIds = partyRepo.findByRatingAndGender(user.getMember().getRank(), user.getMember().getGender());
        for(Long ids : partyIds){
            System.out.println(ids);
        }
        ArrayList<Long> partysAbleToJoin = new ArrayList<>();

        // 리스트가 비어있지 않다면 파티들 중에서 정원 확인
        if(!partyIds.isEmpty()){
            for(Long pid : partyIds){
//                System.out.println(pid);
                List<PartyMember> partyMembers = partyMemberRepo.findAllByPartyId(pid);
//                System.out.println(partyMembers.size());
                if(partyMembers.size() <= 2) partysAbleToJoin.add(pid);
            }
        }
        return partysAbleToJoin; // 일단 party_id arraylist를 넘겨줌
    }

    public Party findMyParty(Long partyId){
        return partyRepo.findByPartyId(partyId);
    }
    

}
