package signiel.heartsigniel.model.user;

import signiel.heartsigniel.Jwt.TokenInfo;

import java.util.List;

public interface UserService {
    TokenInfo login(int userId, List<String> roles, String loginId, String password); // userId를 넣은건 user_roles 테이블에 외래키로써 user_id가 사용되기 때문
    User register(User user) throws Exception;
}
