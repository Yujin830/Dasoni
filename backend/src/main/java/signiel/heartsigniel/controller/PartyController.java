package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.party.*;
import signiel.heartsigniel.model.partyMember.PartyMemberService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PartyController {
    // 파티 정보 다룰 PartyService와 파티 폼을 검증할 PartyFormValidator 주입
    private final PartyService partyService;
    private final PartyMakeService partyMakeService;
    private final PartyMemberService partyMemberService;

//    private final PartyFormValidator partyFormValidator;
//
//    /* @InitBinder : Spring Validator 사용시 @Valid 어노테이션으로 검증이 필요한
//        객체를 가져오기 전에 수행할 method를 지정해주는 어노테이션
//    * */
//    @InitBinder("partyForm")
//    public void partyFormInitBinder(WebDataBinder webDataBinder){
//        webDataBinder.addValidators(partyFormValidator);
//    }
//
//
//    @GetMapping("/party")
//    public String newPartyForm(@AuthenticationPrincipal CustomUserDetails user,
//                               @RequestBody PartyRequest partyRequest){
//        System.out.println("loginId : "+user.getMember().getLoginId()+" "+"role : "+user.getAuthorities());
//        System.out.println("partyRequest info : "+partyRequest.toString());
//        return "PartyForm activated";
//    }
//
//    // 파티 개설 시, 에러가 있을 시 에러 전달, 없을 시 파티 생성
//    @PostMapping("/party")
//    public String newPartySubmit(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PartyRequest partyRequest, Errors errors){
//        System.out.println("PartyController : Before check errors");
//        System.out.println(user.getMember().getLoginId());
//        System.out.println(partyRequest.getPartyId());
//
//        if(errors.hasErrors()){
//            System.out.println("PartyController : 에러발생");
//            return "파티 생성 에러발생";
//        }
//        Party newParty = partyService.createNewParty(user, partyRequest);
//        System.out.println("PartyController : @PostMapping party method activated");
//        System.out.println(newParty.getPartyId());
//        return "newPartySubmit activated";
//    }

    // 파티 찾을 때 rating 에 맞는 파티가 이미 있는지부터 확인해야함 ( 성별 , 레이팅 , 인원 체크 )
    // 파티가 없을 시 생성
    @PostMapping("/party/quick")
    public String findQuickParty(@AuthenticationPrincipal CustomUserDetails user){
        System.out.println("PartyController : findQuickParty method activated");

        List<Long> partysAbleToJoin = partyService.findParties(user);

//        System.out.println("partysAbleToJoin size : "+partysAbleToJoin.size());

        if(partysAbleToJoin.isEmpty()){
            Party newParty = partyMakeService.createNewParty(user);
            System.out.println("PartyController : Make newParty done : "+newParty.toString());
        }
        // 찾기 or 만들기는 됐으니 찾기 이후 있으면 파티 참여 시켜줘야함, 지금은 임시로 0번 인덱스에 참여시켜줌
        partyMemberService.joinToParty(user, partysAbleToJoin.get(0));
        
        return "findQuickParty done";
    }
    

    // 취소 시 빠른매치 파티에서 탈퇴 처리 혹은 인원이 아무도 없어질 시 테이블에서 삭제


}
