package signiel.heartsigniel.model.party.exception;

public class AlreadyJoinedException extends RuntimeException{
    public AlreadyJoinedException(){
        super();
    }

    public AlreadyJoinedException(String message){
        super(message);
    }
}
