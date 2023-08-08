package signiel.heartsigniel.model.meeting.dto;

import lombok.Getter;

@Getter
public class SingleSignalRequest {
    private int signalSequence;
    private Long senderId;
    private Long receiverId;
}
