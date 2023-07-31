package signiel.heartsigniel.model.partyMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.model.partyMember.PartyMember;

import java.util.List;

@Transactional
public interface PartyMemberRepo extends JpaRepository<PartyMember, Long> {
    List<PartyMember> findAllByPartyId(Long partyId);
}
