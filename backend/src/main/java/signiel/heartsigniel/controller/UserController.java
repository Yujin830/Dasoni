package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.user.*;


@RequiredArgsConstructor
@RestController
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        System.out.println("signup "+request.getLoginId());
        System.out.println("test_test");
        return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
    }

    @GetMapping("/users/{loginId}")
    public ResponseEntity<SignResponse> getUser(@PathVariable String loginId) throws Exception {
        return new ResponseEntity<>(userService.getMember(loginId), HttpStatus.OK);
    }
}
