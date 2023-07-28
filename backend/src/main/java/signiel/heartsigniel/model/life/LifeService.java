package signiel.heartsigniel.model.life;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@Transactional
@RequiredArgsConstructor
public class LifeService {
    private final LifeRepo lifeRepo;

    public String insert(Long memberId) throws Exception{
        try {
            Life life = Life.builder()
                    .memberId(memberId)
                    .useDate(LocalDate.now())
                    .build();

            lifeRepo.save(life);
        } catch (Exception e) {
            throw new Exception("잘못된 요청입니다.");
        }
        return "OK";
    }
}
