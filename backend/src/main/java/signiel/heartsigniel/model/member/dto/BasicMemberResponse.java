package signiel.heartsigniel.model.member.dto;

import lombok.Data;

@Data
public class BasicMemberResponse {

    private Long memberId;
    private String nickname;
    private String gender;
    private Long rating;
    private String profileImageSrc;

}
