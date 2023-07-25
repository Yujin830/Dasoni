package signiel.heartsigniel.model.party;

import lombok.Data;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.partymember.PartyMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long avgRating;
    private String gender;

    @OneToMany(mappedBy = "party")
    private List<PartyMember> members = new ArrayList<>();

    // getters and setters

    public void addMember(PartyMember member) {
        members.add(member);
        this.avgRating = calculateAverageRating();
    }

    public void removeMember(PartyMember member) {
        members.remove(member);
        this.avgRating = calculateAverageRating();
    }

    private Long calculateAverageRating() {
        if (members.isEmpty()) {
            return 0L;
        }
        Long sum = members.stream().mapToLong(member -> member.getUser().getRank()).sum();
        return sum / members.size();
    }
}