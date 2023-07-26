package signiel.heartsigniel.model.party;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.partymember.PartyMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 파티 생성 엔티티 클래스
 */
@Entity
@NoArgsConstructor
@Data
public class Party {
    @Id
    @Column(name = "party_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;

    @Column(name="party_gender")
    private String partyGender;

    @Column(name="avg_rating")
    private Long avgRating;

    @Column(name = "party_type", length = 20)
    private String partyType;

    @OneToMany(mappedBy = "party")
    private List<PartyMember> members = new ArrayList<>();

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
