package signiel.heartsigniel.model.guide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.model.guide.dto.GuideDto;

import java.util.Optional;

@Transactional
public interface GuideRepo extends JpaRepository<Guide, Long> {
    Optional<Guide> findByVisibleTime(Long visibleTime);
}
