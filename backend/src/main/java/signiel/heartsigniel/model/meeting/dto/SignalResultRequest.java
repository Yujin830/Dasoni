package signiel.heartsigniel.model.meeting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignalResultRequest {

    private String signalType;
    private List<SingleSignalRequest> signalList;

    public SignalResultRequest(String signalType, List<SingleSignalRequest> signalList){
        this.signalType = signalType;
        this.signalList = signalList;
    }

    @Builder
    public static SignalResultRequest of(String signalType, List<SingleSignalRequest> signalList){
        return new SignalResultRequest(signalType, signalList);
    }

}