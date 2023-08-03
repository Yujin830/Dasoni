package signiel.heartsigniel.model.Token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    boolean existsKeyLoginId(String loginId);
    void deleteByLoginId(String loginId);
}
