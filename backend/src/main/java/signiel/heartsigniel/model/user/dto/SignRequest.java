package signiel.heartsigniel.model.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignRequest {
    private Long memberId;

    private String loginId;

    private String password;

    private String nickname;

    private String gender;

    private Date birth;

    private String phoneNumber;
}
