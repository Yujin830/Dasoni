package signiel.heartsigniel.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
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

    private Date birth;

    private String phoneNumber;

    private boolean isBlack;

    private int rank;

    private int meeting_cnt;

    private String profile_image_src;

    private String job;

    private int siDo;

    private int guGun;

    private List<Authority> roles = new ArrayList<>();

    private String token;

    public SignResponse(Member user){
        this.memberId = user.getMemberId();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.gender = user.getGender();
        this.birth = user.getBirth();
        this.phoneNumber = user.getPhoneNumber();
        this.isBlack = user.isBlack();
        this.rank = user.getRank();
        this.meeting_cnt = user.getMeetingCount();
        this.profile_image_src = user.getProfileImageSrc();
        this.job = user.getJob();
        this.siDo = user.getSiDo();
        this.guGun = user.getGuGun();

        this.roles = user.getRoles();
    }
}
