package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SpecialMemberMatchingResponse {
    private Long roomId;

    public SpecialMemberMatchingResponse(Long roomId){
        this.roomId = roomId;
    }

    @Builder
    public static SpecialMemberMatchingResponse of(Long roomId){
        return new SpecialMemberMatchingResponse(roomId);
    }

}
