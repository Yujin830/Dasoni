package signiel.heartsigniel.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {
    private Long userId;

    private String loginId;

    private String nickname;

    private int age;

//    private String gender;

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

    private List<Authority> roles = new ArrayList<>();

    private String token;

    public SignResponse(User user){
        this.userId = user.getUserId();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.age = user.getAge();
//        this.gender = user.getGender();
        this.roles = user.getRoles();
    }
}
