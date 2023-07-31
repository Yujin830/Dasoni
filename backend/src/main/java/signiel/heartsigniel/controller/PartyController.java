package signiel.heartsigniel.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.party.PartyService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PartyController {
    // 파티 정보 다룰 PartyService와 파티 폼을 검증할 PartyFormValidator 주입
    private final PartyService partyService;


    /* @InitBinder : Spring Validator 사용시 @Valid 어노테이션으로 검증이 필요한
        객체를 가져오기 전에 수행할 method를 지정해주는 어노테이션
    * */





}
