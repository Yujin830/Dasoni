package signiel.heartsigniel.model.room.exception;

public class NotEnteredSettingInformationException extends RuntimeException{
    public NotEnteredSettingInformationException() {
        super();
    }

    public NotEnteredSettingInformationException(String message){
        super(message);
    }
}

