package signiel.heartsigniel.model.party;

import lombok.*;

// NotBlank는 문자열에만 국한된다는 것 주의

/**
 * 파티 생성하려 할 때 정보 전달받는 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PartyRequest {

    private Long partyId;

    private String partyGender;

    private String partyType;

    private Long avgRating;
}
