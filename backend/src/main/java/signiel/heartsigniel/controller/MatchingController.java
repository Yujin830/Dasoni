package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.jwt.CustomUserDetails;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyService;

@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final PartyService partyService;
    @GetMapping("/matching")
    public String fastMatching(@AuthenticationPrincipal CustomUserDetails user, Long partyId){
        // 파티 방 매칭 테이블을 쓸지 , 따로 waiting 테이블을 만들어서 쓸지 고민중


    }
}
