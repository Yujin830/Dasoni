package signiel.heartsigniel.model.user;


import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int userId;
}
