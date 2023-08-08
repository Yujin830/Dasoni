package signiel.heartsigniel.model.meeting.dto;

import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.dto.RoomMemberInfo;

@Getter
public class PersonalResult {
    private RoomMemberInfo roomMemberInfo;
    private Long ratingChange;

    public PersonalResult (RoomMember roomMember, Long ratingChange){
        this.roomMemberInfo = new RoomMemberInfo(roomMember);
        this.ratingChange = ratingChange;
    }

    @Builder
    public static PersonalResult of(RoomMember roomMember, Long ratingChange){
        return new PersonalResult(roomMember, ratingChange);
    }
}

