package signiel.heartsigniel.model.roommember;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.Room;


import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class RoomMember {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "room_id")
        private Room room;

        @ManyToOne
        @JoinColumn(name = "member_id")
        private Member member;

        @Column(columnDefinition = "int default 0")
        private int score = 0;

        @Column(name = "is_special_user")
        private boolean isSpecialUser;

        @Column(name = "is_room_leader")
        private boolean isRoomLeader;

        @Builder
        public static RoomMember of(Member member, Room room, boolean isSpecialUser){
                RoomMember roomMember = new RoomMember();
                roomMember.setRoomLeader(false);
                roomMember.setMember(member);
                roomMember.setScore(0);
                roomMember.setSpecialUser(isSpecialUser);
                roomMember.setRoom(room);
                return roomMember;
        }

}
