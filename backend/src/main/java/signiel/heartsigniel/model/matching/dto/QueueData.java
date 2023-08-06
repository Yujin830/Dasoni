package signiel.heartsigniel.model.matching.dto;

import lombok.Getter;
import signiel.heartsigniel.model.matching.queue.RatingQueue;

@Getter
public class QueueData {
    private String queue;
    private Long memberId;
}
