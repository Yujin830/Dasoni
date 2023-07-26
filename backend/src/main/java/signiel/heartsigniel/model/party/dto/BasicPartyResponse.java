package signiel.heartsigniel.model.party.dto;

import lombok.Data;
import signiel.heartsigniel.model.partymember.dto.BasicPartyMemberResponse;

import java.util.List;

@Data
public class BasicPartyResponse {
    private Long partyId;
    private String gender;
    private List<BasicPartyMemberResponse> members;
}
