package signiel.heartsigniel.model.user;

import signiel.heartsigniel.Jwt.TokenInfo;

public interface UserService {
    // register
    Integer register(User user) throws Exception;

    TokenInfo login(String loginId, String password);
}
