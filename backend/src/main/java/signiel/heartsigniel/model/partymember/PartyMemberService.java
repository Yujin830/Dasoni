package signiel.heartsigniel.model.partymember;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.MatchingPartyService;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.user.Member;

import java.util.List;

@Service
public class PartyMemberService {
    private final PartyMemberRepository partyMemberRepository;
    private final MatchingPartyService matchingPartyService;
    private final MatchingRoomService matchingRoomService;


    public PartyMemberService(PartyMemberRepository partyMemberRepository, MatchingPartyService matchingPartyService, MatchingRoomService matchingRoomService) {
        this.matchingPartyService = matchingPartyService;
        this.partyMemberRepository = partyMemberRepository;
        this.matchingRoomService = matchingRoomService;
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

        if (joinParty == null) {


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

        // If the party has 3 members, try to start a game
        if (joinParty.getMembers().size() == 3) {
            Party matchingParty = partyRepository.findMatchingParty(joinParty.getGender().equals("male") ? "female" : "male", joinParty.getAvgRating());
            if (matchingParty != null) {
                matchingRoomService.createRoom(joinParty, matchingParty);
                matchingRoomService.startGame();
            }
        }

        return joinParty;
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
        Long sum = party.getMembers().stream().mapToLong(member -> member.getMember().getRating()).sum();
        return sum / party.getMembers().size();
    }


}
