package signiel.heartsigniel.model.room.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.party.dto.PartyInfo;
import signiel.heartsigniel.model.question.dto.MatchingQuestionInfo;
import signiel.heartsigniel.model.room.Room;

import javax.servlet.http.Part;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PrivateRoomInfo {

    private Long roomId;
    private String title;
    private String roomType;
    private String videoUrl;
    private Long ratingLimit;
    private LocalDateTime startTime;
    private boolean megiAcceptable;
    private PartyInfo femaleParty;
    private PartyInfo maleParty;
    private List<MatchingQuestionInfo> questions;


    public PrivateRoomInfo(Room roomEntity){
        this.roomId = roomEntity.getId();
        this.title = roomEntity.getTitle();
        this.roomType = roomEntity.getType();
        this.videoUrl = roomEntity.getVideoUrl();
        this.ratingLimit = roomEntity.getRatingLimit();
        this.startTime = roomEntity.getStartTime();
        this.megiAcceptable = roomEntity.isMegiAcceptable();
        this.femaleParty = new PartyInfo(roomEntity.getFemaleParty());
        this.maleParty = new PartyInfo(roomEntity.getMaleParty());
    }

    @Builder
    public static PrivateRoomInfo of(Room roomEntity) {
        return new PrivateRoomInfo(roomEntity);
    }
}

