
package signiel.heartsigniel.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.member.Authority;

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

    private Long siDo;

    private Long guGun;

    private Long isFirst;



    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    private Long remainLife;

    private String token;
}
