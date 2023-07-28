package signiel.heartsigniel.model.room;

import lombok.Data;
import lombok.NoArgsConstructor;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.question.Question;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String type;
    @Column
    private String videoUrl;
    @Column
    private Long ratingLimit;
    @Column
    private String title;

    @Column
    private LocalDateTime startTime;

    @Column
    private boolean megiAcceptable;
    // getters and setters

    @ManyToOne
    private Party maleParty;

    @ManyToOne
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


}