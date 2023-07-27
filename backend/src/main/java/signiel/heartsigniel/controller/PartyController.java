package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyFormValidator;
import signiel.heartsigniel.model.party.PartyRequest;
import signiel.heartsigniel.model.party.PartyService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PartyController {
    // 파티 정보 다룰 PartyService와 파티 폼을 검증할 PartyFormValidator 주입
    private final PartyService partyService;
    private final PartyFormValidator partyFormValidator;

    /* @InitBinder : Spring Validator 사용시 @Valid 어노테이션으로 검증이 필요한
        객체를 가져오기 전에 수행할 method를 지정해주는 어노테이션
    * */
    @InitBinder("partyForm")
    public void partyFormInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(partyFormValidator);
    }


    @GetMapping("/party")
    public String newPartyForm(@AuthenticationPrincipal CustomUserDetails user,
                               @RequestBody PartyRequest partyRequest){
//        System.out.println("loginId : "+user.getUser().getLoginId()+" "+"role : "+user.getAuthorities());
        System.out.println("partyRequest info : "+partyRequest.toString());
        return "PartyForm activated";
    }

    // 파티 개설 시, 에러가 있을 시 에러 전달, 없을 시 파티 생성
    @PostMapping("/party")
    public String newPartySubmit(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PartyRequest partyRequest, Errors errors){
        System.out.println("PartyController : Before check errors");
//        System.out.println(user.getUser().getLoginId());
        System.out.println(partyRequest.getPartyId());
        if(errors.hasErrors()){
            System.out.println("PartyController : 에러발생");
            return "파티 생성 에러발생";
        }
        Party newParty = partyService.createNewParty(user, partyRequest);
        System.out.println("PartyController : @PostMapping party method activated");
        System.out.println(newParty.getPartyId());
        return "newPartySubmit activated";
    }

}
