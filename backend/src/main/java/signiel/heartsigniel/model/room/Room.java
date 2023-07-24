package signiel.heartsigniel.model.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.Nullable;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import signiel.heartsigniel.model.room.dto.RoomOfCreate;
import signiel.heartsigniel.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.net.URL;
import java.time.LocalDateTime;


@Entity
@Getter
@ToString
@RequiredArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long id;

    //방 제목
    @Column(name = "ROOM_TITLE")
    private String roomTitle;

    @Column(name = "MEGI_ACCEPTABLE")
    private boolean megiAcceptable;

    @Column(name = "ROOM_TYPE")
    private boolean roomType;

    @Column(name = "RATING_LIMIT")
    private Long ratingLimit;

    @Column(name = "VIDEO_URL")
    private URL videoURL;

    @Column(name = "ROOM_START_DATE")
    private LocalDateTime startTime;

    @Builder(access = AccessLevel.PRIVATE)
    private Room(RoomOfCreate roomOfCreate){
        this.roomTitle = roomOfCreate.getRoomTitle();
        this.roomType = roomOfCreate.isRoomType();
        this.megiAcceptable = roomOfCreate.isMegiAcceptable();
        this.ratingLimit = roomOfCreate.getRatingLimit();
    }


    public static Room of(RoomOfCreate roomOfCreate){

        return Room.builder()
                .roomOfCreate(roomOfCreate)
                .build();
    }

}
