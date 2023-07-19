package signiel.heartsigniel.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
//    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByLoginId(String loginId);
}
