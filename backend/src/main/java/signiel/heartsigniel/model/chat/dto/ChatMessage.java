package signiel.heartsigniel.model.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {


    private String senderNickname;
    private String content;
}
