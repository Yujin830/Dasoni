package signiel.heartsigniel.model.matching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuickFindResult {
    private Long partyMemberId;

    @Builder
    public QuickFindResult(Long partyMemberId){
        this.partyMemberId = partyMemberId;
    }
}
