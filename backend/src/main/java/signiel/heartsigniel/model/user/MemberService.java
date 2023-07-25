package signiel.heartsigniel.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.jwt.JwtTokenProvider;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignResponse login(SignRequest request) {
        Member user = memberRepo.findByLoginId(request.getLoginId()).orElseThrow(()->
                new BadCredentialsException("잘못된 계정 정보입니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .memberId(user.getMemberId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birth(user.getBirth())
                .phoneNumber(user.getPhoneNumber())
                .isBlack(user.isBlack())
                .rank(user.getRank())
                .meeting_cnt(user.getMeetingCount())
                .profile_image_src(user.getProfileImageSrc())
                .job(user.getJob())
                .siDo(user.getSiDo())
                .guGun(user.getGuGun())
                .roles(user.getRoles())
                .token(jwtTokenProvider.createToken(user.getLoginId(), user.getRoles()))
                .build();
    }

    public boolean register(SignRequest request) throws Exception {
        try {
            System.out.println(request.getLoginId());
            Member user = Member.builder()
                    .loginId(request.getLoginId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .nickname(request.getNickname())
                    .gender(request.getGender())
                    .birth(request.getBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .build();

            user.setRoles(Collections.singletonList(Authority.builder().name("ROLE_GUEST").build()));

            System.out.println(request.getLoginId()+" "+user.getRoles());
            memberRepo.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(String loginId) throws Exception {
        Member user = memberRepo.findByLoginId(loginId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(user);
    }

    public void deleteUserInfo(Long userId) throws Exception{
        memberRepo.deleteById(userId);

    }
}
