package signiel.heartsigniel.model.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.jwt.JwtTokenProvider;

import signiel.heartsigniel.model.Token.RefreshToken;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.member.dto.MemberUpdateDto;
import signiel.heartsigniel.model.member.dto.SignRequest;
import signiel.heartsigniel.model.member.dto.SignResponse;
import signiel.heartsigniel.model.life.Life;
import signiel.heartsigniel.model.life.LifeRepository;


import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final LifeService lifeService;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, LifeService lifeService){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.lifeService = lifeService;
    }


    public SignResponse login(SignRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정 정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        if(member.isBlack())
            throw new LockedException("블랙처리된 사용자입니다.");

        Long remainLives = lifeService.countRemainingLives(member.getMemberId());

        return SignResponse.builder()
                .memberId(member.getMemberId())
                .loginId(member.getLoginId())
                .nickname(member.getNickname())
                .gender(member.getGender())
                .birth(member.getBirth())
                .phoneNumber(member.getPhoneNumber())
                .isBlack(member.isBlack())
                .rating(member.getRating())
                .meetingCount(member.getMeetingCount())
                .profileImageSrc(member.getProfileImageSrc())
                .job(member.getJob())
                .siDo(member.getSiDo())
                .guGun(member.getGuGun())
                .roles(member.getRoles())
                .remainLife(remainLives)
                .token(jwtTokenProvider.createToken(member.getLoginId(), member.getRoles()))
                .build();
    }

    public boolean register(SignRequest request) throws Exception {
        try {
            Member member = Member.builder()
                    .loginId(request.getLoginId())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .gender(request.getGender())
                    .birth(request.getBirth())
                    .phoneNumber(request.getPhoneNumber())
                    .rating(1000L)
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_GUEST").build()));

            System.out.println(request.getLoginId() + " " + member.getRoles());
            memberRepository.save(member);

        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    public String checkDuplicateId(String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() ->
                new InternalAuthenticationServiceException("사용 가능한 아이디입니다."));
        return "이미 존재하는 아이디입니다.";
    }

    public String deleteUserInfo(Long memberId) {
        memberRepository.deleteById(memberId);
        return "OK";
    }

    public String patchMemberPW(Long memberId, SignRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 없습니다."));

        member.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(member);
        return "OK";
    }


    public boolean checkMemberPW(Long memberId, SignRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new UsernameNotFoundException("회원 정보가 없습니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }
        return true;
    }

    public String updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new UsernameNotFoundException("회원 정보가 없습니다."));


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

        memberRepository.save(member);
        return "OK";
    }



}
