package signiel.heartsigniel.model.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleSignalRequest {
    private int signalSequence;
    private int senderId;
    private int receiverId;
}
