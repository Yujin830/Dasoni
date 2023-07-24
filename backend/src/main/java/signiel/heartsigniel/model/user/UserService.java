package signiel.heartsigniel.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.Jwt.JwtTokenProvider;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignResponse login(SignRequest request) throws Exception{
        User user = userRepo.findByLoginId(request.getLoginId()).orElseThrow(()->
                new BadCredentialsException("잘못된 계정 정보입니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .age(user.getAge())
//                .gender(user.getGender())
                .roles(user.getRoles())
                .token(jwtTokenProvider.createToken(user.getLoginId(), user.getRoles()))
                .build();
    }

    public boolean register(SignRequest request) throws Exception {
        try {
            System.out.println(request.getLoginId());
            User user = User.builder()
                    .loginId(request.getLoginId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .age(request.getAge())
//                    .gender(request.getGender())
//                    .birth(request.getBirth())
//                    .phoneNumber(request.getPhoneNumber())
//                    .isBlack(request.isBlack())
//                    .rank(request.getRank())
//                    .meetingCount(request.getMeetingCount())
//                    .profileImageSrc(request.getProfileImageSrc())
//                    .job(request.getJob())
//                    .siDo(request.getSiDo())
//                    .guGun(request.getGuGun())
                    .build();

            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            System.out.println(request.getLoginId()+" "+user.getRoles());
            userRepo.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(String loginId) throws Exception {
        User user = userRepo.findByLoginId(loginId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(user);
    }
}
