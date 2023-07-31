package signiel.heartsigniel.model.member.dto;

import lombok.Getter;
import signiel.heartsigniel.model.member.Member;

import java.time.LocalDate;

@Getter
public class MemberInfo {

    private Long memberId;
    private String nickname;
    private int rating;
    private String gender;
    private String job;
    private LocalDate birth;

    public MemberInfo(Member member){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.rating = member.getRating();
        this.gender = member.getGender();
        this.job = member.getJob();
        this.birth = member.getBirth();
    }

}
