package signiel.heartsigniel.model.member;


import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateDto {
    private String nickname;
    private String profileImageSrc;
    private String job;
    private int siDo;
    private int guGun;
}
