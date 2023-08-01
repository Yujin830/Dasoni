package signiel.heartsigniel.model.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType{
        ENTER, TALK, LEAVE
    }

    private MessageType type;
    private Long roomId;
    private String senderNickname;
    private String content;
}
