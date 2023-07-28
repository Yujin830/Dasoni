package signiel.heartsigniel.model.guide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GuideRepo extends JpaRepository<Guide, Long> {}
