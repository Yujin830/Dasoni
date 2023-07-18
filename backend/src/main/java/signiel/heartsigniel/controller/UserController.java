package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.user.User;
import signiel.heartsigniel.model.user.UserRepo;
import signiel.heartsigniel.model.user.UserService;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;

    @PostMapping("/regist")
    @ResponseStatus(HttpStatus.OK)
    public Integer regist(@Validated @RequestBody User user) throws Exception{
        return userService.register(user);
    }



}
