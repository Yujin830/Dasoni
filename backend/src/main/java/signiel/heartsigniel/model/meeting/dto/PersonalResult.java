package signiel.heartsigniel.model.meeting.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.dto.RoomMemberInfo;

@Getter
@Setter
@NoArgsConstructor
public class PersonalResult {
    private RoomMemberInfo roomMemberInfo;
    private Long ratingChange;
    private int matchMemberId = 0;

    public PersonalResult (RoomMember roomMember, Long ratingChange, int matchedMemberId){
        this.roomMemberInfo = new RoomMemberInfo(roomMember);
        this.ratingChange = ratingChange;
        this.matchMemberId = matchedMemberId;
    }

    @Builder
    public static PersonalResult of(RoomMember roomMember, Long ratingChange, int matchMemberId){
        return new PersonalResult(roomMember, ratingChange, matchMemberId);
    }
}

