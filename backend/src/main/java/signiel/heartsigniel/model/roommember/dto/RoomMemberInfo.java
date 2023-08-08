package signiel.heartsigniel.model.roommember.dto;

import lombok.Getter;
import signiel.heartsigniel.model.member.dto.MemberInfo;
import signiel.heartsigniel.model.roommember.RoomMember;

@Getter
public class RoomMemberInfo {
    private Long partyMemberId;
    private boolean isSpecialUser;
    private boolean isRoomLeader;
    private MemberInfo member;


    public RoomMemberInfo(RoomMember roomMember){
        this.partyMemberId = roomMember.getId();
        this.isSpecialUser = roomMember.isSpecialUser();
        this.isRoomLeader = roomMember.isRoomLeader();
        this.member = new MemberInfo(roomMember.getMember());
    }

}
