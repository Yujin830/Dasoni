package signiel.heartsigniel.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.Jwt.CustomUserDetails;
import signiel.heartsigniel.model.user.User;
import signiel.heartsigniel.model.user.UserRepo;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepo.findByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException("Invaild authentication")
        );
        return new CustomUserDetails(user);
    }
}
