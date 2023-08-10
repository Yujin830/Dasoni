package signiel.heartsigniel.model.chat.dto;

import lombok.*;
import signiel.heartsigniel.model.room.dto.PrivateRoomInfo;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageWithRoomInfo extends ChatMessage{
    private PrivateRoomInfo privateRoomInfo;
}
