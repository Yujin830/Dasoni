package signiel.heartsigniel.model.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import signiel.heartsigniel.model.member.Member;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfo {

    private Long memberId;
    private String nickname;
    private Long rating;
    private String gender;
    private String job;
    private int meetingCount;
    private String profileImageSrc;

    public MemberInfo(Member member){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.rating = member.getRating();
        this.gender = member.getGender();
        this.job = member.getJob();
        this.meetingCount = member.getMeetingCount();
        this.profileImageSrc = member.getProfileImageSrc();
    }

}
