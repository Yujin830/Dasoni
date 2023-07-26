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
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @GetMapping("/users/{memberId}")
    public ResponseEntity<SignResponse> getMember(@PathVariable Long memberId) throws Exception {
        System.out.println("getMember============================");
        return new ResponseEntity<>(memberService.getMember(memberId), HttpStatus.OK);
    }
    
    @DeleteMapping("/users/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.deleteUserInfo(memberId), HttpStatus.OK);
    }

    @PatchMapping("/users/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateDto memberUpdateDto) throws Exception{
        return new ResponseEntity<>(memberService.updateMember(memberId, memberUpdateDto), HttpStatus.OK);
    }

    @PatchMapping("/users/{memberId}/password")
    public ResponseEntity<String> patchMemberPW(@PathVariable Long memberId, @RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.patchMemberPW(memberId, request), HttpStatus.OK);
    }

    @PostMapping("/users/{memberId}/password")
    public ResponseEntity<Boolean> checkMemberPW(@PathVariable Long memberId, @RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.checkMemberPW(memberId, request), HttpStatus.OK);
    }
}
