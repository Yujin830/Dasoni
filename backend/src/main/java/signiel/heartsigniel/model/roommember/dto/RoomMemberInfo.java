package signiel.heartsigniel.model.roommember.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import signiel.heartsigniel.model.member.dto.MemberInfo;
import signiel.heartsigniel.model.roommember.RoomMember;

@Getter
@Setter
@NoArgsConstructor
public class RoomMemberInfo {
    private Long roomMemberId;
    private boolean isSpecialUser;
    private boolean isRoomLeader;
    private MemberInfo member;


    public RoomMemberInfo(RoomMember roomMember){
        this.roomMemberId = roomMember.getId();
        this.isSpecialUser = roomMember.isSpecialUser();
        this.isRoomLeader = roomMember.isRoomLeader();
        this.member = new MemberInfo(roomMember.getMember());
    }

}
