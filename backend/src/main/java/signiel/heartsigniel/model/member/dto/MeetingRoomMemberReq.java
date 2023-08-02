package signiel.heartsigniel.model.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingRoomMemberReq {

    private Long memberId;
    private String nickname;
    private Long rating;
    private String gender;
    private String job;
    private LocalDate birth;
    private int meetingCount;
    private String profileImgSrc;
}
