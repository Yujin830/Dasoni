package signiel.heartsigniel.model.room.dto;


import lombok.Data;
import signiel.heartsigniel.model.member.Member;

@Data
public class PrivateRoomCreateRequest {
    private Member member;
    private String title;
    private Long ratingLimit;
    private boolean megiAcceptable;
}
