package signiel.heartsigniel.model.member.dto;


import lombok.*;

@Getter
@Setter
public class MemberUpdateDto {
    private String nickname;
    private String job;
    private int siDo;
    private int guGun;
    private String profileImageSrc;
}
