package signiel.heartsigniel.model.rating.dto;

import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.member.dto.MemberInfo;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.dto.PartyMemberInfo;

@Getter
public class PersonalResult {
    private PartyMemberInfo partyMemberInfo;
    private Long ratingChange;

    public PersonalResult (PartyMember partyMember, Long ratingChange){
        this.partyMemberInfo = new PartyMemberInfo(partyMember);
        this.ratingChange = ratingChange;
    }

    @Builder
    public static PersonalResult of(PartyMember partyMember, Long ratingChange){
        return new PersonalResult(partyMember, ratingChange);
    }
}

