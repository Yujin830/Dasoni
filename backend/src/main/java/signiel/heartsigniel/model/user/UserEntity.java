package signiel.heartsigniel.model.user;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import signiel.heartsigniel.jpa.BaseTimeEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * UserEntity 정보
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity(name = "user")
public class UserEntity{

    @Id @Column(name = "`user_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name="`login_id`", length = 20)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(unique = true, length = 100)
    private String nickname;

    private int age;

    @Column(length = 10)
    private String gender;

    private Date birth;

    @Column(name = "`phone_number`", length = 100)
    private String phoneNumber;

    @Column(name = "`isblack`")
    private boolean isBlack;

    @Column(name = "`rank`", length = 20)
    private String rank;

    @Column(name = "`meeting_count`")
    private int meetingCount;

    @Column(name = "`profile_image_src`", length = 200)
    private String profileImageSrc;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

}
