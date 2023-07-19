package signiel.heartsigniel.model.user;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

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
@Entity(name = "User")
public class UserEntity implements UserDetails { ///수정

    @Id @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name="login_id", unique = true, length = 20)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(unique = true, length = 10)
    private String nickname;

    private int age;

    @Column(length = 10)
    private String gender;

    private Date birth;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "isblack")
    private boolean isBlack;

    @Column(length = 20)
    private String rank;

    @Column(name = "meeting_count")
    private int meetingCount;

    @Column(name = "profile_image_src", length = 200)
    private String profileImageSrc;

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

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
