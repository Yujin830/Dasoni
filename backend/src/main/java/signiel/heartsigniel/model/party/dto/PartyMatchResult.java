package signiel.heartsigniel.model.party.dto;

import lombok.Getter;
import signiel.heartsigniel.model.party.Party;

@Getter
public class PartyMatchResult {

    private Party femaleParty;
    private Party maleParty;

    public PartyMatchResult(Party femaleParty, Party maleParty){
        this.femaleParty = femaleParty;
        this.maleParty = maleParty;
    }

}
