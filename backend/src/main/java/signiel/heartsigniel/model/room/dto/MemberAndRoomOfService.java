package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.Room;

@Getter
@Builder
public class MemberAndRoomOfService {

    private Member member;
    private Room room;
}
