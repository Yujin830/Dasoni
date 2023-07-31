package signiel.heartsigniel.model.member;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignRequest {
    private Long memberId;

    private String loginId;

    private String password;

    private String gender;

    private Date birth;

    private String phoneNumber;
}
