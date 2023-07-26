package signiel.heartsigniel.model.party;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.user.Member;

import javax.servlet.http.Part;

@Service
public class PrivatePartyService implements PartyService{

    @Override
    public Party createParty(Member member){
        Party party = new Party();
        party.setGender(member.getGender());
        party.setPartyType("private");
        party.setAvgRating(member.getRating());
        return party;
    }
}
