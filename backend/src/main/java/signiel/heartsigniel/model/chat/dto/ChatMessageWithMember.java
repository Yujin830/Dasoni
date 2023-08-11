package signiel.heartsigniel.model.chat.dto;


import lombok.*;
import signiel.heartsigniel.model.member.Member;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageWithMember extends ChatMessage{
    private List<Member> memberList;
}
