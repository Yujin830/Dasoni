package signiel.heartsigniel.model.party.dto;

import lombok.Getter;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.partymember.dto.PartyMemberInfo;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PartyInfo {

    private Long partyId;
    private String gender;
    private Long avgRating;
    private List<PartyMemberInfo> partyMembers;

    public PartyInfo(Party partyEntity) {
        this.partyId = partyEntity.getPartyId();
        this.gender = partyEntity.getPartyGender();
        this.avgRating = partyEntity.getAvgRating();
        this.partyMembers = partyEntity.getMembers().stream()
                .map(PartyMemberInfo::new)
                .collect(Collectors.toList());
    }
}
