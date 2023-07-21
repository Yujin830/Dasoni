package signiel.heartsigniel.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.Jwt.TokenInfo;
import signiel.heartsigniel.model.user.User;
import signiel.heartsigniel.model.user.UserEntity;
import signiel.heartsigniel.model.user.UserService;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/regist")
    @ResponseStatus(HttpStatus.OK)
    public User regist(@Validated @RequestBody User user) throws Exception{
        return userService.register(user);
    }

    @PostMapping("/login")
    public TokenInfo login(@RequestBody UserEntity userEntity){
//        int userId = userEntity.getUserId();
        String loginId = userEntity.getLoginId();
        String password = userEntity.getPassword();
        return userService.login(userEntity.getUserId(), userEntity.getRoles(), loginId, password);
    }

    @GetMapping("/test")
    public String test(){
        return "Ok";
    }
}
