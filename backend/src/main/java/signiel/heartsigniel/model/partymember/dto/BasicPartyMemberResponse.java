package signiel.heartsigniel.model.partymember.dto;

import lombok.Data;
import signiel.heartsigniel.model.member.dto.BasicMemberResponse;

import java.util.ArrayList;
import java.util.List;

@Data
public class BasicPartyMemberResponse {
    private Long partyMemberId;
    private boolean isPartyLeader;
    private boolean isSpecialUser;
    private List<BasicMemberResponse> members = new ArrayList<>();

}
