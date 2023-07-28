package signiel.heartsigniel.model.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PartyRepository extends JpaRepository<Party, Long> {
    boolean existsByPartyId(Long partyId);
}
