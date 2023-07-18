package signiel.heartsigniel.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {
    Optional<User> findByLoginId(String loginId);
}
