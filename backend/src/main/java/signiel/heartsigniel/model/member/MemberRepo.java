package signiel.heartsigniel.model.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface MemberRepo extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId);
}


