package signiel.heartsigniel.model.party.exception;

public class FullPartyException extends RuntimeException{
    public FullPartyException(){
        super();
    }

    public FullPartyException(String message){
        super(message);
    }
}
