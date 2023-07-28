package signiel.heartsigniel.model.warn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface WarnRepo extends JpaRepository<Warn, Long> {
    List<Warn> findAllByMemberId(Long memberId);
}
