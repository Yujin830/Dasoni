package signiel.heartsigniel.model.guide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface GuideRepository extends JpaRepository<Guide, Long> {
    Optional<Guide> findByVisibleTime(Long visibleTime);
}
