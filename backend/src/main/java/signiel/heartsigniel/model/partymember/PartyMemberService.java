package signiel.heartsigniel.model.partymember;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;

@Service
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;
    private final PartyRepository partyRepository;

    public PartyMemberService(PartyMemberRepository partyMemberRepository, PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
        this.partyMemberRepository = partyMemberRepository;
    }

    public void leaveParty(PartyMember member) {
        Party party = member.getParty();
        party.getMembers().remove(member);
        partyMemberRepository.delete(member);

        if (member.isPartyLeader()) {
            if (party.getMembers().isEmpty()) {
                partyRepository.delete(party);
            } else {
                PartyMember newLeader = party.getMembers().get(0);
                newLeader.setPartyLeader(true);
                partyMemberRepository.save(newLeader);
            }
        }

        party.setAvgRating(calculateAverageRating(party));
        partyRepository.save(party);
    }

    private Long calculateAverageRating(Party party) {
        if (party.getMembers().isEmpty()) {
            return 0L;
        }
        Long sum = party.getMembers().stream().mapToLong(member -> member.getUser().getRank()).sum();
        return sum / party.getMembers().size();
    }


}
