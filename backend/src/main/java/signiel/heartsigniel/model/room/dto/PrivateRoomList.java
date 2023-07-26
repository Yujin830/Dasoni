package signiel.heartsigniel.model.room.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.room.Room;

@Getter
public class PrivateRoomList {

    private Long roomId;
    private String roomTitle;
    private Long malePartyMemberCount;
    private Long femalePartyMemberCount;
    private Long malePartyAvgRating;
    private Long femalePartyAvgRating;

    public PrivateRoomList(){

    }

    @Builder(access = AccessLevel.PRIVATE)
    private PrivateRoomList(Room room) {
        this.roomId = room.getId();
        this.roomTitle = room.getTitle();
        this.malePartyAvgRating = room.getMaleParty().getAvgRating();
        this.femalePartyAvgRating = room.getFemaleParty().getAvgRating();
        this.malePartyMemberCount = (long) room.getMaleParty().getMembers().size();
        this.femalePartyMemberCount = (long) room.getFemaleParty().getMembers().size();
    }

    public static PrivateRoomList of(Room room){
        return PrivateRoomList.builder()
                .room(room)
                .build();
    }
}
