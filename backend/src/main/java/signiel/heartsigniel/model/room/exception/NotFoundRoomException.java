package signiel.heartsigniel.model.room.exception;

public class NotFoundRoomException extends RuntimeException{
    public NotFoundRoomException() {
        super();
    }

    public NotFoundRoomException(String message){
        super(message);
    }

}
