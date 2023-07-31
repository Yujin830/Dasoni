package signiel.heartsigniel.model.partyMember;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PartyMemberRequest {
    private Long partyId;

    private Long memberId;

    private Boolean megi;

    private Boolean partyManager;
}
