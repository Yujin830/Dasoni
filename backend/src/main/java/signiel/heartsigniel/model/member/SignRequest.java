package signiel.heartsigniel.model.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SignRequest {
    private Long memberId;

    private String loginId;

    private String password;

    private String gender;

    private LocalDate birth;

    private String phoneNumber;
}
