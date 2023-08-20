package signiel.heartsigniel.model.member.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SignRequest {

    @NotNull(message = "아이디를 입력해주세요.")
    private String loginId;

    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotNull(message = "성별을 선택해주세요.")
    private String gender;

    @NotNull(message = "생년월일을 선택해주세요")
    private LocalDate birth;

    @NotNull(message = "전화번호를 입력해주세요.")
    private String phoneNumber;
}
