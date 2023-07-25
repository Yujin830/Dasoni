package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.member.*;


@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        System.out.println("signup "+request.getLoginId());
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @GetMapping("/users/{loginId}")
    public ResponseEntity<SignResponse> getMember(@PathVariable String loginId) throws Exception {
        return new ResponseEntity<>(memberService.getMember(loginId), HttpStatus.OK);
    }
    
    @DeleteMapping("/users/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) throws  Exception {
        memberService.deleteUserInfo(memberId);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }



}
