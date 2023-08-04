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

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "party_id")
        private Party party;

        @ManyToOne
        @JoinColumn(name = "member_id")
        private Member member;

        @Column(columnDefinition = "int default 0")
        private int score;

        @Column(name = "is_party_leader")
        private boolean isPartyLeader;

        @Column(name = "is_special_user")
        private boolean isSpecialUser;

        @Column(name = "is_room_leader")
        private boolean isRoomLeader;

        @Builder PartyMember(boolean isPartyLeader, boolean isSpecialUser, boolean isRoomLeader, Member member){
                this.isPartyLeader = isPartyLeader;
                this.member = member;
                this.isSpecialUser = isSpecialUser;
                this.isRoomLeader = isRoomLeader;
        }

}
