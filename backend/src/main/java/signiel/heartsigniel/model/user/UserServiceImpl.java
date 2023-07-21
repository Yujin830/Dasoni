package signiel.heartsigniel.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.Jwt.JwtTokenProvider;
import signiel.heartsigniel.Jwt.TokenInfo;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public User register(User user) throws Exception {
        if(userRepo.findByLoginId(user.getLoginId()).isPresent()){
            throw  new Exception("이미 존재하는 아이디 입니다.");
        }

        UserEntity userEntity = userRepo.save(user.toEntity());
        userEntity.encodePassword(passwordEncoder);

//        userEntity.addUserAuthority();
        return user;
    }

    @Transactional
    public TokenInfo login(int userId, List<String> roles, String loginId, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
