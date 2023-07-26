package signiel.heartsigniel.model.room.dto;

import lombok.Data;
import signiel.heartsigniel.model.party.Party;

@Data
public class PrivateRoomCreateResponse {
    private Long roomId;
    private String roomType;
    private String videoUrl;
    private Long ratingLimit;
    private boolean megiAcceptable;
    private Party maleParty;
    private Party femaleParty;
}