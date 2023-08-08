package signiel.heartsigniel.model.guide;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GuideService {
    private final GuideRepository guideRepository;
    public List<Guide> selectAll(){
        return guideRepository.findAll();
    }
}
