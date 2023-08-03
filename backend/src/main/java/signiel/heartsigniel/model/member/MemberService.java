package signiel.heartsigniel.model.member;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.jwt.JwtTokenProvider;

import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.member.dto.MemberUpdateDto;
import signiel.heartsigniel.model.member.dto.SignRequest;
import signiel.heartsigniel.token.RefreshTokenRepository;
import signiel.heartsigniel.token.dto.RefreshToken;
import signiel.heartsigniel.token.dto.TokenDto;
import signiel.heartsigniel.token.response.GlobalResDto;


import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final LifeService lifeService;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, LifeService lifeService, RefreshTokenRepository refreshTokenRepository){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.lifeService = lifeService;
        this.refreshTokenRepository = refreshTokenRepository;
    }


    public GlobalResDto login(SignRequest request, HttpServletResponse response) {
        Member member = memberRepository.findByLoginId(request.getLoginId()).orElseThrow(() ->
                new BadCredentialsException("잘못된 계정 정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }

        if(member.isBlack())
            throw new LockedException("블랙처리된 사용자입니다.");
        TokenDto tokenDto = jwtTokenProvider.createAllToken(request.getLoginId());
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByLoginId(request.getLoginId());
        Long remainLives = lifeService.countRemainingLives(member.getMemberId());

        if(refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        }else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), request.getLoginId());
            refreshTokenRepository.save(newToken);
        }

        // response 헤더에 Access Token / Refresh Token 넣음
        response.addHeader(jwtTokenProvider.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(jwtTokenProvider.REFRESH_TOKEN, tokenDto.getRefreshToken());

        return new GlobalResDto("Success Login", HttpStatus.OK.value());
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
