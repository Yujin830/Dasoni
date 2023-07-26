package signiel.heartsigniel.model.party;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import signiel.heartsigniel.model.user.Member;

import javax.persistence.*;

/**
 * 파티 생성 엔티티 클래스
 */
@Entity
@NoArgsConstructor
@Getter
public class Party {
    @Id
    @Column(name = "party_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;

    @Column(name="party_gender")
    private String partyGender;

    @Column(name="avg_rating")
    private int avgRating;

    @Column(name = "party_type", length = 20)
    private String partyType;

    public static Party from(PartyRequest partyRequest){
        System.out.println("Party.java / 'from' activated");
        Party party = new Party();
        party.partyId = partyRequest.getPartyId();
        party.partyType = partyRequest.getPartyType();
        party.partyGender = partyRequest.getPartyGender();
        party.avgRating = partyRequest.getAvgRating();
        return party;
    }

    public void addManager(Long memberId){
        System.out.println("Party.java / 'addManager' activated");
    }

}
