package signiel.heartsigniel.model.user;


import lombok.*;
import signiel.heartsigniel.jpa.BaseTimeEntity;
import javax.validation.constraints.NotBlank;

/**
 * UserDto
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Data
public class User extends BaseTimeEntity {

    private Long userId;
    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;
    private String password;
    private String nickname;
    private int age;
    private String gender;
    private String date;
    private String phoneNumber;
    private boolean isBlack;
    private String rank;
    private int meetingCount;
    private String profileImageSrc;

}
