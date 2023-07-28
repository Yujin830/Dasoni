package signiel.heartsigniel.model.room.dto;


import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.party.Party;

@Getter
@Builder
public class PartyByRoomService {
    private Party maleParty;
    private Party femaleParty;
}
