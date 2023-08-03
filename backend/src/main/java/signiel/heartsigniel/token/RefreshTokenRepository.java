package signiel.heartsigniel.token;

import org.springframework.data.jpa.repository.JpaRepository;
import signiel.heartsigniel.token.dto.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByLoginId(String loginId);
}