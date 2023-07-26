package signiel.heartsigniel.model.member.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignRequest {
    private Long memberId;

    private String loginId;

    private String password;

    private String gender;

    private Date birth;

    private String phoneNumber;
}
