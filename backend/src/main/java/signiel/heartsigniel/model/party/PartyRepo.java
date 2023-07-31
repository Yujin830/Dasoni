package signiel.heartsigniel.model.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PartyRepo extends JpaRepository<Party, Long> {
    boolean existsByPartyId(Long partyId);

    Party findByPartyId(Long partyId);

    @Query("SELECT p.partyId FROM party p WHERE p.partyGender = :gender AND p.avgRating < :rank + 100 AND :rank - 100 < p.avgRating ")
    List<Long> findByRatingAndGender(int rank, String gender);

}
