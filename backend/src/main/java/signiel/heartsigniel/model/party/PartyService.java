package signiel.heartsigniel.model.party;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.user.Member;
import signiel.heartsigniel.model.user.MemberRepo;
import signiel.heartsigniel.model.user.UserRepo;

import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepo memberRepo;
    private final PartyMemberRepository partyMemberRepository;

    public PartyService(PartyRepository partyRepository, MemberRepo memberRepo, PartyMemberRepository partyMemberRepository){
        this.partyRepository = partyRepository;
        this.memberRepo = memberRepo;
        this.partyMemberRepository = partyMemberRepository;
    }

    public Party findPartyAndJoinOrCreateParty(Member member) {
        List<Party> parties = partyRepository.findByGender(member.getGender());
        Party joinParty = null;
        for (Party pt : parties) {
            if (pt.getMembers().size() <= 2 && Math.abs(pt.getAvgRating() - member.getRating()) <= 100) {
                joinParty = pt;
                break;
            }
        }

        // If no suitable party is found, create a new one
        if (joinParty == null) {
            joinParty = new Party();
            joinParty.setAvgRating(member.getRating());
            joinParty.setGender(member.getGender());
            partyRepository.save(joinParty);
        }

        boolean isPartyLeader = joinParty.getMembers().isEmpty();
        boolean isSpecialUser = joinParty.getMembers().size() == 3;
        PartyMember partyMember = new PartyMember();
        partyMember.setMember(member);
        partyMember.setParty(joinParty);
        partyMember.setPartyLeader(isPartyLeader);
        partyMember.setSpecialUser(isSpecialUser);
        partyMemberRepository.save(partyMember);

        joinParty.setAvgRating(calculateAverageRating(joinParty));

        partyRepository.save(joinParty);
        return joinParty;
    }

    private Long calculateAverageRating(Party party) {
        if (party.getMembers().isEmpty()) {
            return 0L;
        }
        Long sum = party.getMembers().stream().mapToLong(member -> member.getMember().getRating()).sum();
        return sum / party.getMembers().size();
    }

}