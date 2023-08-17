package signiel.heartsigniel.model.meeting.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TotalResultResponse {
    private List<PersonalResult> personalResults;
    private Long roomId;

    public TotalResultResponse(List<PersonalResult> personalResults, Long roomId) {
        this.personalResults = personalResults;
        this.roomId = roomId;
    }

    @Builder
    public static TotalResultResponse of(List<PersonalResult> personalResults, Long roomId){
        return new TotalResultResponse(personalResults, roomId);
    }
}