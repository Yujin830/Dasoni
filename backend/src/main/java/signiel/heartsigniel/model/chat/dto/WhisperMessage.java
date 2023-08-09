package signiel.heartsigniel.model.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WhisperMessage {
    private String memberId;
    private String receiverId;
    private String content;
    private String status;
}
