package signiel.heartsigniel.model.party;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.user.Member;

@Service
public class MatchingPartyService implements PartyService {

    private final PartyRepository partyRepository;
    private final MatchingRoomService matchingRoomService;

    public MatchingPartyService(PartyRepository partyRepository, MatchingRoomService matchingRoomService){
        this.partyRepository = partyRepository;
        this.matchingRoomService = matchingRoomService;
    }

    @Override
    public Party createParty(Member member) {
        Party party = new Party();
        party.setAvgRating(member.getRating());
        party.setGender(member.getGender());
        party.setPartyType("Match");
        partyRepository.save(party);
        return party;
    }

    public void tryMatchParty(Party party) {
        // If the party has 3 members, try to start a game
        if (party.getMembers().size() == 3) {
            Party matchingParty = partyRepository.findMatchingParty(party.getGender().equals("male") ? "female" : "male", party.getAvgRating());
            if (matchingParty != null) {
                matchingRoomService.createRoom(party, matchingParty);
                matchingRoomService.startGame();
            }
        }
    }

    private Long calculateAverageRating(Party party) {
        if (party.getMembers().isEmpty()) {
            return 0L;
        }
        Long sum = party.getMembers().stream().mapToLong(member -> member.getMember().getRating()).sum();
        return sum / party.getMembers().size();
    }

}