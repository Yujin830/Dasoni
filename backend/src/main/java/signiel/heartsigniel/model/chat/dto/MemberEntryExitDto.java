package signiel.heartsigniel.model.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntryExitDto {
    private Long memberId;
    private String type;
}
