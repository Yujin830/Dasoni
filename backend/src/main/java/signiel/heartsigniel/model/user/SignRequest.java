package signiel.heartsigniel.model.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
public class SignRequest {
    private Long userId;

    private String loginId;

    private String password;

    private String nickname;

    private int age;

//    private String gender;
//
//    private Date birth;
//
//    private String phoneNumber;
//
//    private boolean isBlack;
//
//    private int rank;
//
//    private int meetingCount;
//
//    private String profileImageSrc;
//
//    private String job;
//
//    private int siDo;
//
//    private int guGun;
}
