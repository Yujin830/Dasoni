package signiel.heartsigniel.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepo extends JpaRepository<User, Long> {
//    Optional<UserEntity> findByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
}
