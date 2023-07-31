package signiel.heartsigniel.model.partymember;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.party.Party;


import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
public class PartyMember {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        private Party party;

        @ManyToOne
        private Member member;

        private boolean isPartyLeader;
        private boolean isSpecialUser;

        @Builder PartyMember(boolean isPartyLeader, boolean isSpecialUser, Member member){
                this.isPartyLeader = isPartyLeader;
                this.member = member;
                this.isSpecialUser = isSpecialUser;
        }

}
