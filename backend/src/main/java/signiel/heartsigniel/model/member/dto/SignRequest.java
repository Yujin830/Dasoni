package signiel.heartsigniel.model.member.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SignRequest {
    private Long memberId;

    private String loginId;

    private String password;

    private String gender;

    private LocalDate birth;

    private String phoneNumber;
}
