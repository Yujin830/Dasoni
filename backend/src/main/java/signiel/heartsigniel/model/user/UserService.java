package signiel.heartsigniel.model.user;

import signiel.heartsigniel.Jwt.TokenInfo;

public interface UserService {
    TokenInfo login(String loginId, String password);
    User register(User user) throws Exception;
}
