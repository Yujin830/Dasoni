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
    private Long remainLife;

    public PersonalResult (RoomMember roomMember, Long ratingChange, int matchedMemberId, Long remainLife){
        this.roomMemberInfo = new RoomMemberInfo(roomMember);
        this.ratingChange = ratingChange;
        this.matchMemberId = matchedMemberId;
        this.remainLife = remainLife;
    }

    @Builder
    public static PersonalResult of(RoomMember roomMember, Long ratingChange, int matchMemberId, Long remainLife){
        return new PersonalResult(roomMember, ratingChange, matchMemberId, remainLife);
    }
}

