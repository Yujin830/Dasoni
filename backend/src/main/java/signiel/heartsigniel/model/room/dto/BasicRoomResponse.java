package signiel.heartsigniel.model.room.dto;

import lombok.Data;

import signiel.heartsigniel.model.party.dto.BasicPartyResponse;

@Data
public class BasicRoomResponse {

    private Long roomId;
    private BasicPartyResponse maleParty;
    private BasicPartyResponse femaleParty;

}
