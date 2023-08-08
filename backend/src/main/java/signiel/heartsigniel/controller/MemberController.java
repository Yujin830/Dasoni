package signiel.heartsigniel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.*;
import signiel.heartsigniel.model.member.dto.MemberUpdateDto;
import signiel.heartsigniel.model.member.dto.SignRequest;
import signiel.heartsigniel.model.member.dto.SignResponse;
import signiel.heartsigniel.model.meeting.RatingService;

@RestController
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final RatingService ratingService;

    public MemberController(MemberService memberService, RatingService ratingService){
        this.memberService = memberService;
        this.ratingService = ratingService;
    }

    @PostMapping("/api/login")
    public ResponseEntity<SignResponse> signin(@RequestBody SignRequest request) {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }


    @PostMapping("/api/register")
    public ResponseEntity<Boolean> signup(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(memberService.register(request), HttpStatus.OK);
    }

    @PostMapping("/api/register/{loginId}")
    public ResponseEntity<String> getMember(@PathVariable String loginId) {
        return new ResponseEntity<>(memberService.checkDuplicateId(loginId), HttpStatus.OK);
    }

    @DeleteMapping("/api/users/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.deleteUserInfo(memberId), HttpStatus.OK);
    }

    @PatchMapping("/api/users/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateDto
            memberUpdateDto) {
        return new ResponseEntity<>(memberService.updateMember(memberId, memberUpdateDto), HttpStatus.OK);
    }

    @PatchMapping("/api/users/{memberId}/password")
    public ResponseEntity<String> patchMemberPW(@PathVariable Long memberId, @RequestBody SignRequest request) {
        return new ResponseEntity<>(memberService.patchMemberPW(memberId, request), HttpStatus.OK);
    }

    @PostMapping("/api/users/{memberId}/password")
    public ResponseEntity<Boolean> checkMemberPW(@PathVariable Long memberId, @RequestBody SignRequest request) {
        return new ResponseEntity<>(memberService.checkMemberPW(memberId, request), HttpStatus.OK);
    }

    @GetMapping("/api/users/{memberId}/history")
    public ResponseEntity<Response> getMatchingHistory(@PathVariable Long memberId){
        Response response = Response.of(CommonCode.GOOD_REQUEST, ratingService.getMatchedMemberIds(memberId));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/users/{memberId}/history")
    public ResponseEntity<Response> deleteMatchingHistory(@PathVariable Long memberId){
        ratingService.deleteMatchingHistory(memberId);
        Response response = Response.of(CommonCode.GOOD_REQUEST, null);
        return ResponseEntity.ok(response);
    }
}