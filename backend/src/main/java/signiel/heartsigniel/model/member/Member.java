package signiel.heartsigniel.model.member;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MemberEntity 정보
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "member")
public class Member {

    @Id @Column(name = "`member_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(name="`login_id`", unique = true, length = 20)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(length = 10)
    private String nickname;

    @Column(length = 10)
    private String gender;

    private Date birth;

    @Column(name = "`phone_number`", length = 20)
    private String phoneNumber;

    @Column(name = "`isblack`")
    private boolean isBlack;

    @Column(name = "`rank`")
    private int rank;

    @Column(name = "`meeting_cnt`")
    private int meetingCount;

    @Column(name = "`profile_image_src`", length = 200)
    private String profileImageSrc;

    @Column(name = "`job`", length = 20)
    private String job;

    @Column(name = "si_do")
    private int siDo;

    @Column(name = "gu_gun")
    private int guGun;


    @OneToMany(mappedBy = "member", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role){
        this.roles = role;
        role.forEach(o->o.setMember(this));
    }
}
