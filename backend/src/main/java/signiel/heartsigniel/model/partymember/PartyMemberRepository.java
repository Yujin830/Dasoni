package signiel.heartsigniel.model.partymember;

import org.springframework.data.jpa.repository.JpaRepository;
import signiel.heartsigniel.model.member.Member;

import java.util.List;
import java.util.Optional;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    Optional<PartyMember> findByMember_MemberIdAndParty_PartyId(Long memberId, Long partyId);

    List<PartyMember> findByParty_PartyId(Long partyId);
    Optional<PartyMember> findPartyMemberByMember(Member member);
}