package signiel.heartsigniel.model.partyMember;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class PartyMemberCompositeKey implements Serializable {
    private Long partyId;
    private Long memberId;
}
