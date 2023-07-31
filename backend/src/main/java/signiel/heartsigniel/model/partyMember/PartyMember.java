package signiel.heartsigniel.model.partyMember;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name="party_member")
@NoArgsConstructor
@Getter
@IdClass(PartyMemberCompositeKey.class) // 복합 키 클래스 연결
public class PartyMember {
    @Id
    @Column(name = "party_id")
    private Long partyId;

    @Id
    @Column(name="member_id")
    private Long memberId;

    @Column(name="megi")
    @ColumnDefault("false")
    private boolean megi;

    @Column(name="party_manager")
    @ColumnDefault("false")
    private boolean partyManager;

    public PartyMember(Long partyId, Long memberId, boolean megi, boolean partyManager) {
        this.partyId=partyId;
        this.memberId=memberId;
        this.megi=megi;
        this.partyManager=partyManager;
    }


    public static PartyMember from(PartyMemberRequest partyMemberRequest){
        System.out.println("PartyMember.java / 'from' activated");
        PartyMember partyMember = new PartyMember();
        partyMember.partyId = partyMemberRequest.getPartyId();
        partyMember.memberId = partyMemberRequest.getMemberId();
        partyMember.megi = partyMemberRequest.getMegi();
        partyMember.partyManager = partyMemberRequest.getPartyManager();
        return partyMember;
    }
}
