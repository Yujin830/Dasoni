package signiel.heartsigniel.model.partymember;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyMemberRepository extends JpaRepository<PartyMember, Long> {
    Optional<PartyMember> findByMember_MemberIdAndParty_PartyId(Long memberId, Long partyId);}