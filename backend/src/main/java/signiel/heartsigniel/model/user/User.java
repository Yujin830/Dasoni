package signiel.heartsigniel.model.user;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UserEntity 정보
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "user")
public class User {

    @Id @Column(name = "`user_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name="`login_id`", unique = true, length = 20)
    private String loginId;

    @Column(length = 100)
    private String password;

    private String nickname;

    private int age;

//    @Column(length = 10)
//    private String gender;
//
//    @Column(name = "birth")
//    private Date birth;
//
//    @Column(name = "`phone_number`", length = 100)
//    private String phoneNumber;
//
//    @Column(name = "`isblack`")
//    private boolean isBlack;
//
//    @Column(name = "`rank`")
//    private int rank;
//
//    @Column(name = "`meeting_count`")
//    private int meetingCount;
//
//    @Column(name = "`profile_image_src`", length = 200)
//    private String profileImageSrc;
//
//    @Column(name = "`job`", length = 20)
//    private String job;
//
//    @Column(name = "si_do")
//    private int siDo;
//
//    @Column(name = "gu_gun")
//    private int guGun;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role){
        this.roles = role;
        role.forEach(o->o.setUser(this));
    }
}
