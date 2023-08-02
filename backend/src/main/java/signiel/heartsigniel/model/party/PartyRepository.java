package signiel.heartsigniel.model.party;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Transactional
public interface PartyRepository extends JpaRepository<Party, Long> {
    @Query("SELECT p FROM party p WHERE ABS(p.avgRating - :averageRating) <= 100 AND p.partyGender = :partyGender AND SIZE(p.members) <= :membersCount AND p.partyType = 'match'")
    List<Party> findMatchPartyByAvgRatingAndPartyGenderAndMembersCount(@Param("averageRating") Long averageRating, @Param("partyGender") String partyGender, @Param("membersCount") Integer membersCount);
    @Query("SELECT p FROM party p WHERE " +
            "p.partyGender <> :partyGender AND " +
            "ABS(p.avgRating - :avgRating) <= 100 AND " +
            "SIZE(p.members) = 3 AND " +
            "p.partyType = 'match' " +
            "ORDER BY p.matchingTime ASC")
    List<Party> findOldestMatchPartyByOppositeGenderAndAvgRatingAndMembersCount(
            @Param("partyGender") String partyGender,
            @Param("avgRating") Long avgRating
    );

}

