package signiel.heartsigniel.model.user;


import lombok.*;

@Getter
@Setter
@ToString(exclude = "password")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;
}
