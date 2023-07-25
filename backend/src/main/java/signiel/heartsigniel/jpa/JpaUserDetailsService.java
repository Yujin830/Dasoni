package signiel.heartsigniel.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.user.Member;
import signiel.heartsigniel.model.user.MemberRepo;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final MemberRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member user = userRepo.findByLoginId(loginId).orElseThrow(
                () -> new UsernameNotFoundException("Invaild authentication")
        );
        return new CustomUserDetails(user);
    }
}
