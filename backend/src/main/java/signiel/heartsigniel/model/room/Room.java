package signiel.heartsigniel.model.room;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.party.Party;


import javax.persistence.*;
import java.time.LocalDateTime;


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


    @ManyToOne(cascade = CascadeType.REMOVE)
    private Party maleParty;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Party femaleParty;

    public boolean isGameStarted() {
        return startTime.isBefore(LocalDateTime.now());
    }

    public boolean isGameFinished() {
        return startTime.plusHours(5 / 6).isBefore(LocalDateTime.now());
    }

    public Long roomMemberCount() {
        return (long) (maleParty.getMembers().size() + femaleParty.getMembers().size());
    }

    public Long femaleMemberCount() {
        return (long) femaleParty.getMembers().size();
    }

    public Long maleMemberCount() {
        return (long) maleParty.getMembers().size();
    }

}