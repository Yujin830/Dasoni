package signiel.heartsigniel.model.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchMemberMessage {
    private String memberId;
    private String receiverId;
    private String senderNickname;
    private String content;
}
