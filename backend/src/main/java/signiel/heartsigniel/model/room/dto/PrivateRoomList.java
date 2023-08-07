package signiel.heartsigniel.model.room.dto;

import lombok.Getter;
import signiel.heartsigniel.model.room.Room;


@Getter
public class PrivateRoomList {

    private Long roomId;
    private String title;
    private Long femaleMemberCount;
    private Long maleMemberCount;
    private Long femaleAvgRating;
    private Long maleAvgRating;
    private Long ratingLimit;

    public PrivateRoomList(Room roomEntity){
        this.roomId = roomEntity.getId();
        this.title = roomEntity.getTitle();
        this.ratingLimit = roomEntity.getRatingLimit();
        this.femaleAvgRating = roomEntity.memberAvgRatingByGender("female");
        this.maleMemberCount = roomEntity.memberCountByGender("male");
        this.femaleMemberCount = roomEntity.memberCountByGender("female");
        this.maleAvgRating = roomEntity.memberAvgRatingByGender("male");;

    }

    public static PrivateRoomList of(Room roomEntity){
        return new PrivateRoomList(roomEntity);
    }
}