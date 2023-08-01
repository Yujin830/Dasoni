package signiel.heartsigniel.model.party;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.party.dto.PartyMatchResult;
import signiel.heartsigniel.model.party.exception.FullPartyException;
import signiel.heartsigniel.model.party.exception.NoPartyLeaderException;
import signiel.heartsigniel.model.party.exception.NoPartyMemberException;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.partymember.PartyMemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PartyService {

    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final PartyMemberService partyMemberService;

    public PartyService(PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, PartyMemberService partyMemberService){
        this.partyRepository = partyRepository;
        this.partyMemberService = partyMemberService;
        this.partyMemberRepository = partyMemberRepository;
    }


    public Party createParty(String partyType, String partyGender){

        Party party = Party.builder()
                .partyType(partyType)
                .partyGender(partyGender)
                .build();

        partyRepository.save(party);

        return party;
    }

    public PartyMember joinParty(Party party, Member member){

        if (party.getMembers() == null) {
            party.setMembers(new ArrayList<>());
        }
        if (party.getMembers().size() == 3){
            throw new FullPartyException("인원이 가득 찼습니다.");
        }
        boolean isPartyLeader = party.getMembers().isEmpty();
        PartyMember partyMember = partyMemberService.createPartyMember(member, isPartyLeader);

        party.getMembers().add(partyMember);
        partyMember.setParty(party);
        if (isPartyFull(party) | getPartyType(party,  "match")){
            party.setMatchingTime();
        }

        CalculatePartyRating(party);
        return partyMember;
    }

    public void quitParty(PartyMember partyMember){
        Party partyEntity = partyMember.getParty();
        List<PartyMember> targetPartyMemberList = findPartyMemberByParty(partyEntity);
        // 파티멤버가 파티장인 경우
        if (partyMember.isPartyLeader()){
            // 파티원이 한명인 경우
            if(targetPartyMemberList.size() == 1){
                partyMemberService.deletePartyMember(partyMember);
                partyEntity.removePartyMember(partyMember);
                if(partyEntity.getPartyType().equals("match")){
                    deleteParty(partyEntity);
                }
            }
            // 파티원이 두명 이상인 경우
            else if(targetPartyMemberList.size() >= 2){
                partyMemberService.deletePartyMember(partyMember);
                partyEntity.removePartyMember(partyMember);
                PartyMember newPartyLeader = targetPartyMemberList.get(0); // 0번쨰는 탈퇴한 멤버일 것이므로 1번째 멤버를 새로운 파티장으로 지정
                partyMemberService.delegatePartyLeader(newPartyLeader);
                CalculatePartyRating(partyEntity);// 새로운 파티장 지정
                if(targetPartyMemberList.size() == 3){
                    partyEntity.setMatchingTime(null);
                    CalculatePartyRating(partyEntity);// 파티원이 세명일 경우에는 파티의 matchingTime을 초기화
                }
            }
        }
        // 파티장이 아닌 경우
        else {
            partyMemberService.deletePartyMember(partyMember);
            partyEntity.removePartyMember(partyMember);
            if(targetPartyMemberList.size() == 3){
                partyEntity.setMatchingTime(null);
                CalculatePartyRating(partyEntity);
            }
        }
    }

    public Optional<PartyMatchResult> matchParty(Party party){
        List<Party> optionalMatchedParty = partyRepository.findOldestMatchPartyByOppositeGenderAndAvgRatingAndMembersCount(party.getPartyGender(), party.getAvgRating());

        if (!optionalMatchedParty.isEmpty()) {
            // 매칭된 파티가 있을 경우
            if (party.getPartyGender().equals("female")){
                return Optional.of(new PartyMatchResult(party, optionalMatchedParty.get(0)));
            }
            return Optional.of(new PartyMatchResult(optionalMatchedParty.get(0), party));

        } else {
            // 매칭된 파티가 없을 경우
            return Optional.empty();
        }
    }

    // 파티와 유저 아이디로 파티 멤버 객체를 찾는 메서드
    public PartyMember findPartyMemberByMemberIdAndPartyId(Long memberId, Long partyId) {
        return partyMemberRepository.findByMember_MemberIdAndParty_PartyId(memberId, partyId)
                .orElseThrow(() -> new NoPartyMemberException("파티멤버가 존재하지 않습니다."));
    }

    public PartyMember findPartyLeaderByParty(Party targetParty){
        List<PartyMember> partyMembers = targetParty.getMembers();
        System.out.println("기모링"+ partyMembers.size());
        for (PartyMember partyMember : partyMembers) {
            if(partyMember.isPartyLeader()){
                return partyMember;
            }
        }

        throw new NoPartyLeaderException("파티장이 없는 파티입니다.");
    }

    public Party findSuitableParties(Member targetMember){
        String gender = targetMember.getGender();
        Long rating = targetMember.getRating();

        List<Party> suitableParties =  partyRepository.findMatchPartyByAvgRatingAndPartyGenderAndMembersCount(rating, gender, 2);

        if (suitableParties.isEmpty()){
            return createParty("match", gender);
        }

        return suitableParties.get(0);
    }

    // 파티 멤버 찾기
    public List<PartyMember> findPartyMemberByParty(Party party){
        List<PartyMember> partyMemberList = party.getMembers();
        return partyMemberList;
    }

    // 파티의 레이팅을 계산
    public void CalculatePartyRating(Party party){
        party.calculateAndSetAvgRating();
        partyRepository.save(party);
    }

    public boolean isPartyFull(Party party) {
        int matchingMemberCount = 3;
        return party.getMembers().size() == matchingMemberCount;
    }

    public void deleteParty(Party party){
        partyRepository.delete(party);
    }

    public boolean getPartyType(Party party, String partyType){
        if (party.getPartyType() == partyType){
            return true;
        }
        return false;
    }

}
