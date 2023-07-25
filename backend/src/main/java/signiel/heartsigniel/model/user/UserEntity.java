package signiel.heartsigniel.model.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import signiel.heartsigniel.model.partymember.PartyMember;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserEntity 정보
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "user")
public class UserEntity implements UserDetails {

    @Id @Column(name = "`user_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name="`login_id`", unique = true, length = 20)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(unique = true, length = 100)
    private String nickname;

    private int age;

    @Column(length = 10)
    private String gender;

    @Column(name = "birth")
    private Date birth;

    @Column(name = "`phone_number`", length = 100)
    private String phoneNumber;

    @Column(name = "`isblack`")
    private boolean isBlack;

    @Column(name = "`rank`")
    private Long rank;

    @Column(name = "`meeting_count`")
    private int meetingCount;

    @Column(name = "`profile_image_src`", length = 200)
    private String profileImageSrc;

    @Column(name = "`job`", length = 20)
    private String job;

    @Column(name = "si_do")
    private int siDo;

    @Column(name = "gu_gun")
    private int guGun;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PartyMember> partyMembers = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //////
    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
