package signiel.heartsigniel.model.roommember.exception;

public class NotFoundRoomMemberException extends RuntimeException{
    public NotFoundRoomMemberException() {
        super();
    }

    public NotFoundRoomMemberException(String message){
        super(message);
    }
}
