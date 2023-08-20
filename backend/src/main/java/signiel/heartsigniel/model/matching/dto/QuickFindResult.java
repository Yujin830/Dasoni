package signiel.heartsigniel.model.matching.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuickFindResult {
    private Long roomMemberId;

    @Builder
    public QuickFindResult(Long partyMemberId){
        this.roomMemberId = partyMemberId;
    }
}
