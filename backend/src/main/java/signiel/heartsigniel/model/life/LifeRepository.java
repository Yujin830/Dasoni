package signiel.heartsigniel.model.life;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface LifeRepository extends JpaRepository<Life, Long> {
    List<Life> findByMember_MemberIdAndUseDate(Long memberId, LocalDate useDate);
    List<Life> findByMember_MemberIdAndUseDateBefore(Long memberId, LocalDate date);
}
