package signiel.heartsigniel.model.life;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
public interface LifeRepo extends JpaRepository<Life, Long> {
    List<Life> findByMemberIdAndUseDate(Long memberId, LocalDate useDate);
}
