package signiel.heartsigniel.model.party.exception;

public class NoPartyMemberException extends RuntimeException{
    public NoPartyMemberException() {
        super();
    }

    public NoPartyMemberException(String message){
        super(message);
    }
}
