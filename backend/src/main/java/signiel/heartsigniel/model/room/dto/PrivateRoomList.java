package signiel.heartsigniel.model.room.dto;

import lombok.Builder;
import lombok.Getter;
import signiel.heartsigniel.model.room.Room;


@Getter
public class PrivateRoomList {

    private Long roomId;
    private String title;
    private Long femalePartyMemberCount;
    private Long malePartyMemberCount;
    private Long femalePartyAvgRating;
    private Long malePartyAvgRating;
    private Long ratingLimit;

    @Builder
    public PrivateRoomList(Room roomEntity){
        this.roomId = roomEntity.getId();
        this.title = roomEntity.getTitle();
        this.femalePartyMemberCount = roomEntity.femaleMemberCount();
        this.malePartyMemberCount = roomEntity.maleMemberCount();
        this.ratingLimit = roomEntity.getRatingLimit();
        this.femalePartyAvgRating = roomEntity.getFemaleParty().getAvgRating();
        this.malePartyAvgRating = roomEntity.getMaleParty().getAvgRating();
    }
}