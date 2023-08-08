package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.roommember.dto.RoomMemberInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MatchingRoomInfo {

    private Long roomId;
    private String title;
    private String roomType;
    private String videoUrl;
    private Long ratingLimit;
    private LocalDateTime startTime;
    private boolean megiAcceptable;
    private List<RoomMemberInfo> roomMemberInfoList;

    public MatchingRoomInfo(Room roomEntity){
        this.roomId = roomEntity.getId();
        this.title = roomEntity.getTitle();
        this.roomType = roomEntity.getRoomType();
        this.videoUrl = roomEntity.getVideoUrl();
        this.ratingLimit = roomEntity.getRatingLimit();
        this.startTime = roomEntity.getStartTime();
        this.megiAcceptable = roomEntity.isMegiAcceptable();
        this.roomMemberInfoList = roomEntity.getRoomMembers().stream()
                .map(RoomMemberInfo::new)
                .collect(Collectors.toList());
    }
    @Builder
    public static MatchingRoomInfo of(Room roomEntity) {
        return new MatchingRoomInfo(roomEntity);
    }
}

