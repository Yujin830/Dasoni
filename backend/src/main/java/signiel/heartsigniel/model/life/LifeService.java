package signiel.heartsigniel.model.life;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.model.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class LifeService {
    private final LifeRepository lifeRepository;

    public LifeService(LifeRepository lifeRepository){
        this.lifeRepository = lifeRepository;
    }

    public void useLife(Member member){

        // 요청이 들어올때마다
        deleteExpirationTimeStamp(member.getMemberId());

        // 남은 목숨 있을 경우 목숨 사용
        if (countRemainingLives(member.getMemberId()) >=1){
            Life life = Life.builder()
                    .member(member)
                    .useDate(LocalDate.now())
                    .build();

            lifeRepository.save(life);
        }
    }

    public Long countRemainingLives(Long memberId){
        return 2L - lifeRepository.findByMember_MemberIdAndUseDate(memberId, LocalDate.now()).size();
    }

    public void deleteExpirationTimeStamp(Long memberId) {
        List<Life> expirationList = lifeRepository.findByMember_MemberIdAndUseDateBefore(memberId, LocalDate.now());

        if (!expirationList.isEmpty()) {
            lifeRepository.deleteAll(expirationList);
        }
    }

}
