
package signiel.heartsigniel.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.member.Authority;
import signiel.heartsigniel.model.member.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {
    private Long memberId;

    private String loginId;

    private String nickname;

    private String gender;

    private LocalDate birth;

    private String phoneNumber;

    private boolean isBlack;

    private Long rating;

    private int meetingCount;

    private String profileImageSrc;

    private String job;

    private int siDo;

    private int guGun;

    private List<Authority> roles = new ArrayList<>();

    private int remainLife;

    private String token;

    public SignResponse(Member member){
        this.memberId = member.getMemberId();
        this.loginId = member.getLoginId();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.birth = member.getBirth();
        this.phoneNumber = member.getPhoneNumber();
        this.isBlack = member.isBlack();
        this.rating = member.getRating();
        this.meetingCount = member.getMeetingCount();
        this.profileImageSrc = member.getProfileImageSrc();
        this.job = member.getJob();
        this.siDo = member.getSiDo();
        this.guGun = member.getGuGun();

        this.roles = member.getRoles();
    }
}
