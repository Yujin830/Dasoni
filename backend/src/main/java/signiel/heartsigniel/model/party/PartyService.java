package signiel.heartsigniel.model.party;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.user.User;
import signiel.heartsigniel.model.user.UserRepo;

import java.util.List;

@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final UserRepo userRepo;
    private final PartyMemberRepository partyMemberRepository;

    public PartyService(PartyRepository partyRepository, UserRepo userRepo, PartyMemberRepository partyMemberRepository){
        this.partyRepository = partyRepository;
        this.userRepo = userRepo;
        this.partyMemberRepository = partyMemberRepository;
    }

    public Party findPartyAndJoinOrCreateParty(User user) {
        List<Party> parties = partyRepository.findByGender(user.getGender());
        Party joinParty = null;
        for (Party pt : parties) {
            if (pt.getMembers().size() <= 2 && Math.abs(pt.getAvgRating() - user.getRank()) <= 100) {
                joinParty = pt;
                break;
            }
        }

        // If no suitable party is found, create a new one
        if (joinParty == null) {
            joinParty = new Party();
            joinParty.setAvgRating(user.getRank());
            joinParty.setGender(user.getGender());
            partyRepository.save(joinParty);
        }

        boolean isPartyLeader = joinParty.getMembers().isEmpty();
        boolean isSpecialUser = joinParty.getMembers().size() == 3;
        PartyMember member = new PartyMember();
        member.setUser(user);
        member.setParty(joinParty);
        member.setPartyLeader(isPartyLeader);
        member.setSpecialUser(isSpecialUser);
        partyMemberRepository.save(member);

        joinParty.setAvgRating(calculateAverageRating(joinParty));

        partyRepository.save(joinParty);
        return joinParty;
    }

    private Long calculateAverageRating(Party party) {
        if (party.getMembers().isEmpty()) {
            return 0L;
        }
        Long sum = party.getMembers().stream().mapToLong(member -> member.getUser().getRank()).sum();
        return sum / party.getMembers().size();
    }

}