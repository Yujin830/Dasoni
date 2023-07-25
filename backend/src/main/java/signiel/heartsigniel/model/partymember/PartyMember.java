package signiel.heartsigniel.model.partymember;

import lombok.Data;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.user.User;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class PartyMember {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "party_id")
        private Party party;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        private boolean isPartyLeader;
        private boolean isSpecialUser;



}
