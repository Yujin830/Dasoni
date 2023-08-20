package signiel.heartsigniel.model.chat.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String senderNickname;
    private String content;

}
