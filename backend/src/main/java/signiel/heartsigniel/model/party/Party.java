package signiel.heartsigniel.model.party;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.RequiredArgsConstructor;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.partymember.PartyMember;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 파티 생성 엔티티 클래스
 */
@Entity(name = "party")
@Data
@RequiredArgsConstructor
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

    @Column(name="matching_time")
    private LocalDateTime matchingTime;

    @Builder
    public Party(String partyGender, Long avgRating, String partyType, List<PartyMember> members){
        this.partyGender = partyGender;
        this.avgRating = avgRating;
        this.partyType = partyType;
        this.members = members;
    }

    public void setMatchingTime() {
        this.matchingTime = LocalDateTime.now();
    }

    public void calculateAndSetAvgRating() {
        if (members.isEmpty()) {
            this.avgRating = 0L;
        } else {
            long sum = 0;
            for (PartyMember member : members) {
                sum += member.getMember().getRating(); // Assuming PartyMember has a 'getRating' method
            }
            this.avgRating = sum / members.size();
        }
    }
    public void addManager(Long memberId){
        System.out.println("Party.java / 'addManager' activated");
    }

    public void removePartyMember(PartyMember partyMember) {
        this.members.remove(partyMember);
        partyMember.setParty(null);
    }

}
