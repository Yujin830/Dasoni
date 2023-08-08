package signiel.heartsigniel.model.meeting.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class TotalResultRequest {
    @NotNull(message = "방 ID는 필수입니다.")
    private Long roomId;

    @NotNull(message = "방 타입은 필수입니다.")
    private String roomType;

    @NotNull(message = "신호 결과는 필수입니다.")
    private List<SignalResultRequest> signalResults;
}
