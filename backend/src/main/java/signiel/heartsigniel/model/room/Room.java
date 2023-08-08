package signiel.heartsigniel.model.room;

        import lombok.Data;
        import lombok.NoArgsConstructor;
        import signiel.heartsigniel.model.member.Member;
        import signiel.heartsigniel.model.roommember.RoomMember;

        import javax.persistence.*;
        import java.time.LocalDateTime;
        import java.util.List;


@Data
@NoArgsConstructor
@Entity(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String roomType;
    @Column
    private Long ratingLimit;
    @Column
    private String title;
    @Column
    private LocalDateTime startTime;
    @Column
    private String videoUrl;
    @Column
    private boolean megiAcceptable;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<RoomMember> roomMembers;

    public boolean isGameStarted() {
        return startTime.isBefore(LocalDateTime.now());
    }

    public boolean isGameFinished() {
        return startTime.plusHours(5 / 6).isBefore(LocalDateTime.now());
    }

    public Long roomMemberCount() {
        return (long) roomMembers.size();
    }

    // 성별에 따른 방 인원 구하기
    public Long memberCountByGender(String gender){
        Long genderCount = 0L;

        for(RoomMember roomMember : roomMembers){
            if(roomMember.getMember().getGender().equals(gender)){
                genderCount += 1;
            }
        }
        return genderCount;
    }

    // 성별에 따른 방 평균 레이팅 구하기
    public Long memberAvgRatingByGender(String gender){

        Long avgRating = 0L;

        for(RoomMember roomMember : roomMembers){
            if(roomMember.getMember().getGender().equals(gender)){
                avgRating += roomMember.getMember().getRating();
            }
        }
        return memberCountByGender(gender) >0? avgRating/memberCountByGender(gender) : 0;
    }
}