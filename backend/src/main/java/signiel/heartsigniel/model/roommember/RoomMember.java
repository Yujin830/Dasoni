package signiel.heartsigniel.model.roommember;

import lombok.Builder;
import lombok.Data;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.Room;


import javax.persistence.*;

@Entity
@Data
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
        private int score;

        @Column(name = "is_special_user")
        private boolean isSpecialUser;

        @Column(name = "is_room_leader")
        private boolean isRoomLeader;

        @Builder RoomMember(boolean isSpecialUser, boolean isRoomLeader, Member member){
                this.member = member;
                this.isSpecialUser = isSpecialUser;
                this.isRoomLeader = isRoomLeader;
        }

}
