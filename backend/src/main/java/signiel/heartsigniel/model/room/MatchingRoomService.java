package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.user.Member;

@Service
public class MatchingRoomService implements RoomService{

    private final RoomRepository roomRepository;
    private final PartyRepository partyRepository;

    public MatchingRoomService(RoomRepository roomRepository, PartyRepository partyRepository) {
        this.roomRepository = roomRepository;
        this.partyRepository = partyRepository;

    }

    @Override
    public Room createRoom(Party maleParty,Party femaleParty) {
        Room room = new Room();
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);
        roomRepository.save(room);
        return room;
    }

    @Override
    public void deleteRoom(Room room){
        if (room.isGameFinished()) {
            roomRepository.delete(room);
        }
    }

    @Override
    public void startGame(Room room){
        room.setStartTime(LocalDateTime.now());
        roomRepository.save(room);
    }

    @Override
    public void endGame(Room room) {
        if (room.isGameStarted()) {
            room.setStartTime(room.getStartTime().plusHours(1));
            roomRepository.save(room);
        }
    }

    @Override
    public Room joinRoom(Member member, Party party){
        throw new UnsupportedOperationException();
    }

    @Override
    public Room exitRoom(Member member, Party party){
        throw new UnsupportedOperationException();
    }

    @Override
    public Room settingRoom(Room room, boolean megiAcceptable, Long ratingLimit){
        room.setMegiAcceptable(megiAcceptable);
        room.setRatingLimit(ratingLimit);
        roomRepository.save(room);
        return room;
    }

}
