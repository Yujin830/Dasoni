package signiel.heartsigniel.model.party.exception;

public class PartyNotFoundException extends RuntimeException{
    public PartyNotFoundException() {
        super();
    }

    public PartyNotFoundException(String message){
        super(message);
    }
}
