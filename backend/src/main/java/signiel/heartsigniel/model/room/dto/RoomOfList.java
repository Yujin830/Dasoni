package signiel.heartsigniel.model.room.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.room.Room;


/**
 * 방 목록 생성을 위한 Dto
 */

@Getter
public class RoomOfList {

    private Long roomId;
    private String roomTitle;
    private Boolean megiAcceptable;


    @Builder(access = AccessLevel.PRIVATE)
    private RoomOfList(Room room){
        this.roomId = room.getId();
        this.roomTitle = room.getRoomTitle();
        this.megiAcceptable = room.isMegiAcceptable();
    }
}
