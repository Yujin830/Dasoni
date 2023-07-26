package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrivateRoomOfCreated {

    private Long createdRoomId;
    private Long malePartyId;
    private Long femalePartyId;

}