package signiel.heartsigniel.model.party.exception;

public class NoPartyLeaderException extends RuntimeException{
    public NoPartyLeaderException() {
        super();
    }

    public NoPartyLeaderException(String message){
        super(message);
    }
}
