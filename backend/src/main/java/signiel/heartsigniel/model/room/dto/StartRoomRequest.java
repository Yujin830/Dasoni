package signiel.heartsigniel.model.room.dto;

import lombok.Getter;

@Getter
public class StartRoomRequest {
    private Long roomLeaderId;
    private Long roomId;
}
