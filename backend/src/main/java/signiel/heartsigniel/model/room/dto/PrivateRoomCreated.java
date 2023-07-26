package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class PrivateRoomCreated {
    private Long createdRoomId;
}
