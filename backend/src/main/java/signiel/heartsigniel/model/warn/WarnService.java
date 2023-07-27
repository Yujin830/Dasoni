package signiel.heartsigniel.model.warn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepo;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WarnService {
    private final WarnRepo warnRepo;
    private final MemberRepo memberRepo;

    public int insertAndSelect(Long memberId) throws Exception{
        try{
            Warn warn = Warn.builder()
                    .memberId(memberId)
                    .warnDate(LocalDate.now())
                    .build();

            warnRepo.save(warn);
        } catch(Exception e){
            throw new Exception("잘못된 요청입니다.");
        }
        //추가

        List<Warn> warn = warnRepo.findAllByMemberId(memberId);
        int size = warn.size();
        //누적 경고가 몇개인지

        if(size==3){
            Member member = memberRepo.findById(memberId)
                    .orElseThrow(() -> new Exception("계정이 없습니다."));
            member.setBlack(true);
            memberRepo.save(member);
        }
        //만약 3번 경고면 isBlack=true 처리

        return size;
    }
}
