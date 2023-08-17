package signiel.heartsigniel.model.member.dto;

import lombok.Getter;

@Getter
public class LifeAndMeetingCountResponse {
    private Long remainLife;
    private int meetingCount;

    public LifeAndMeetingCountResponse(Long remainLife, int meetingCount){
        this.meetingCount = meetingCount;
        this.remainLife = remainLife;
    }

    public static LifeAndMeetingCountResponse of(Long remainLife, int meetingCount){
        return new LifeAndMeetingCountResponse(remainLife,meetingCount);
    }
}
