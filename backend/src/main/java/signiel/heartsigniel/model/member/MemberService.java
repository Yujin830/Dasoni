package signiel.heartsigniel.model.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.jwt.JwtTokenProvider;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepo memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public SignResponse login(SignRequest request) {
        Member member = memberRepo.findByLoginId(request.getLoginId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정 정보입니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return SignResponse.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .birth(member.getBirth())
                .phoneNumber(member.getPhoneNumber())
                .isBlack(member.isBlack())
                .rank(member.getRank())
                .meetingCount(member.getMeetingCount())
                .profileImageSrc(member.getProfileImageSrc())
                .job(member.getJob())
                .siDo(member.getSiDo())
                .guGun(member.getGuGun())
                .roles(member.getRoles())
                .token(jwtTokenProvider.createToken(member.getLoginId(), member.getRoles()))
                .build();
    }

    public boolean register(SignRequest request) throws Exception {
        try {
            System.out.println(request.getLoginId());
            Member member = Member.builder()
                    .loginId(request.getLoginId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .gender(request.getGender())
                    .birth(request.getBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_GUEST").build()));

            System.out.println(request.getLoginId() + " " + member.getRoles());
            memberRepo.save(member);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public SignResponse getMember(Long memberId) throws Exception {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new SignResponse(member);
    }


    public String deleteUserInfo(Long memberId) {
        memberRepo.deleteById(memberId);
        return "OK";
    }

    public String patchMemberPW(Long memberId, SignRequest request) throws Exception {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepo.save(member);
        return "OK";
    }

    public boolean checkMemberPW(Long memberId, SignRequest request) throws Exception{
        Member member = memberRepo.findById(memberId)
                .orElseThrow(()->new Exception("계정을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 비밀번호입니다.");
        }
        return true;
    }

    public String updateMember(Long memberId, MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(()->new Exception("계정을 찾을 수 없습니다."));

        if(member.getJob()==null){
            List<Authority> list = member.getRoles();
            for(Authority authority:list)
                authority.setName("ROLE_USER");
            member.setRoles(list);
        }

        member.setNickname(memberUpdateDto.getNickname());
        member.setJob(memberUpdateDto.getJob());
        member.setSiDo(memberUpdateDto.getSiDo());
        member.setGuGun(memberUpdateDto.getGuGun());
        member.setProfileImageSrc(memberUpdateDto.getProfileImageSrc());

        memberRepo.save(member);
        return "OK";
    }
}
