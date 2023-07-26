package signiel.heartsigniel.model.party;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// Spring form 유효성 검사
@Component
@RequiredArgsConstructor
public class PartyFormValidator implements Validator {
    private final PartyRepo partyRepo;

    // 어떤 타입의 객체 검증 시, 이 객체의 클래스가 validator가 검증할 수 있는 클래스인지 판단하는 메소드
    @Override
    public boolean supports(Class<?> clazz){
        return PartyRequest.class.isAssignableFrom(clazz);
    }

    // 실제 검증이 이루어지는 메소드
    @Override
    public void validate(Object target, Errors errors){
        PartyRequest partyRequest = (PartyRequest) target;
        if(partyRepo.existsByPartyId(partyRequest.getPartyId())){
            // getPartyId는 임시로 만든 메소드 (추후 수정 필요)
            errors.rejectValue("partyId","duplicatedId","이미 사용중인 파티 아이디입니다.");
        }
    }
}
