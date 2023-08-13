package signiel.heartsigniel.model.member.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MemberUpdateDto {
    private String nickname;
    private String job;
    private String siDo;
    private String guGun;
//    private String profileImageSrc;
}
