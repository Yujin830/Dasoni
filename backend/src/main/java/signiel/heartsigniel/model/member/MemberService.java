package signiel.heartsigniel.model.member;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import signiel.heartsigniel.jwt.JwtTokenProvider;

import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.member.dto.MemberUpdateDto;
import signiel.heartsigniel.model.member.dto.SignRequest;
import signiel.heartsigniel.model.member.dto.SignResponse;


import java.util.*;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final LifeService lifeService;
    private final ImageService imageService;
    private final ImageRepo imageRepo;
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, LifeService lifeService, ImageService imageService, ImageRepo imageRepo){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.lifeService = lifeService;
        this.imageService = imageService;
        this.imageRepo = imageRepo;
    }


    public SignResponse login(SignRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId()).orElseThrow(() ->
                new UsernameNotFoundException("잘못된 계정 정보입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        }
        if(member.isBlack())
            throw new LockedException("블랙처리된 사용자입니다.");

        Long remainLives = lifeService.countRemainingLives(member.getMemberId());

        Long loginCount = member.getIsFirst();

        if(member.getIsFirst() != 2){
            loginCount++;
            member.setIsFirst(loginCount);
        }

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
                .isFirst(member.getIsFirst())
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
                    .isFirst(0L)
                    .profileImageSrc("null")
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

    public SignResponse memberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("회원 정보가 없습니다."));

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
                .build();
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

    public String updateMember(Long memberId, MemberUpdateDto memberUpdateDto, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new UsernameNotFoundException("회원 정보가 없습니다."));


        if(member.getJob()==null){
            List<Authority> list = member.getRoles();
            for(Authority authority:list)
                authority.setName("ROLE_USER");
            member.setRoles(list);
        }

        System.out.println(file.getContentType());

        member.setNickname(memberUpdateDto.getNickname());
        member.setJob(memberUpdateDto.getJob());
        member.setSiDo(memberUpdateDto.getSiDo());
        member.setGuGun(memberUpdateDto.getGuGun());
        member.setProfileImageSrc(imageService.saveImage(file));

        memberRepository.save(member);
        return member.getProfileImageSrc();
    }

    public String updateProfileImage(Long memberId, MultipartFile image){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new UsernameNotFoundException("회원 정보가 없습니다."));

        if(member.getJob()==null){
            List<Authority> list = member.getRoles();
            for(Authority authority:list)
                authority.setName("ROLE_USER");
            member.setRoles(list);
        }

        if(member.getProfileImageSrc() == "null"){
            member.setProfileImageSrc(imageService.saveImage(image));
            memberRepository.save(member);
            return "Update OK";
        }else{
//            Image memberImage = imageRepo.findImageByAccessUrl(member.getProfileImageSrc());
//            System.out.println("memberImage : "+ memberImage.getOriginName());
//            imageService.deleteImage(memberImage.getStoredName());
            member.setProfileImageSrc(imageService.saveImage(image));
            memberRepository.save(member);
            return "Changed OK";
        }
    }

}
