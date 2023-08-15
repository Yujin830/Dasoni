package signiel.heartsigniel.model.meeting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignalMatchingResult {
    private Long memberId;
    private Long opponentId;
    private String profileImageUrl;

    public SignalMatchingResult() {}

    public SignalMatchingResult(Long memberId, Long opponentId, String profileImageUrl){
        this.memberId = memberId;
        this.opponentId = opponentId;
        this.profileImageUrl = profileImageUrl;
    }

    @Builder
    public static SignalMatchingResult of(Long memberId, Long opponentId, String profileImageUrl){
        return new SignalMatchingResult(memberId, opponentId, profileImageUrl);
    }
}
