package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.dto.PartyMatchResult;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class MatchingRoomService {

    private final RoomRepository roomRepository;

    public MatchingRoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Room createRoom(PartyMatchResult partyMatchResult) {
        Room room = new Room();

        Party femaleParty = partyMatchResult.getFemaleParty();
        Party maleParty = partyMatchResult.getMaleParty();

        room.setFemaleParty(femaleParty);
        room.setMaleParty(maleParty);
        room.setRoomType("matching");
        room = roomRepository.save(room);

        room.setRatingLimit(0L);
        room.setTitle(room.getId() + "번 자동매칭방");
        room.setMegiAcceptable(true);
        room.setStartTime(LocalDateTime.now());

        return room;
    }



}
