package signiel.heartsigniel.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public Integer register(User user) throws Exception {
        if(userRepo.findByLoginId(user.getLoginId()).isPresent()){
            throw  new Exception("이미 존재하는 아이디 입니다.");
        }

        UserEntity userEntity = userRepo.save(user.toEntity());
        userEntity.encodePassword(passwordEncoder);

        userEntity.addUserAuthority();
        return userEntity.getUserId();
    }
}
