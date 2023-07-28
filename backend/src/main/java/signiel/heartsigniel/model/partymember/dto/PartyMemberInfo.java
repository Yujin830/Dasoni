package signiel.heartsigniel.model.partymember.dto;

import lombok.Getter;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.dto.MemberInfo;
import signiel.heartsigniel.model.partymember.PartyMember;

@Getter
public class PartyMemberInfo {
    private Long partyMemberId;
    private boolean isPartyLeader;
    private boolean isSpecialUser;
    private MemberInfo member;

    public PartyMemberInfo (PartyMember partyMember){
        this.partyMemberId = partyMember.getId();
        this.isPartyLeader = partyMember.isPartyLeader();
        this.isSpecialUser = partyMember.isSpecialUser();
        this.member = new MemberInfo(partyMember.getMember());}

}
