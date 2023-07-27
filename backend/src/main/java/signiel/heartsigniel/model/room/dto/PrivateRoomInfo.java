package signiel.heartsigniel.model.room.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.room.Room;

@Getter
public class PrivateRoomInfo {

    private String roomId;
    private String title;
    private String


    @Builder(access = AccessLevel.PRIVATE)
    public PrivateRoomInfo(Room roomEntity){

    }
    public static PrivateRoomInfo of(Room roomEntity) {
        return PrivateRoomInfo.builder()
    }
}

