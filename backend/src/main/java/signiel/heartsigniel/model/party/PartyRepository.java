package signiel.heartsigniel.model.party;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
    Party findFirstByAvgRatingAndGender(Long rating, String gender);
    List<Party> findByGender(String gender);

}
