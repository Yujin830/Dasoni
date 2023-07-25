package signiel.heartsigniel.model.user;


import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * UserDto
 */


@Data
@Builder
@AllArgsConstructor
public class User{

    @NotBlank(message = "아이디를 입력해주세요")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;
    private String nickname;
    private int age;
    @NotBlank(message = "성별을 입력해 주세요.")
    private String gender;
    private Date birth;
    private String phoneNumber;
    private boolean isBlack;
    private Long rank;
    private int meetingCount;
    private String profileImageSrc;
    @NotBlank(message = "직업을 입력해주세요.")
    private String job;
    @NotBlank(message = "지역을 선택해주세요.")
    private int siDo;
    @NotBlank(message = "지역을 선택해주세요.")
    private int guGun;

    @Builder
    public UserEntity toEntity(){
        return UserEntity.builder()
                .loginId(loginId)
                .password(password)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .isBlack(isBlack)
                .rank(rank)
                .meetingCount(meetingCount)
                .profileImageSrc(profileImageSrc)
                .job(job)
                .siDo(siDo)
                .guGun(guGun)
                .build();
    }



}
