package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PrivateRoomService implements RoomService{

    private final RoomRepository roomRepository;
    private final QuestionRepository questionRepository;
    private final PartyRepository partyRepository;

    public PrivateRoomService(RoomRepository roomRepository, QuestionRepository questionRepository, PartyRepository partyRepository) {

        this.questionRepository = questionRepository;
        this.roomRepository = roomRepository;
        this.partyRepository = partyRepository;
    }


    @Override
    public Room createRoom() {
        Room room = new Room();
        Party maleParty = new Party();
        maleParty.setGender("male");
        Party femaleParty = new Party();
        femaleParty.setGender("female");
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);
        partyRepository.save(maleParty);
        partyRepository.save(femaleParty);
        roomRepository.save(room);
        return room;
    }

    @Override
    public void startGame(Room room) {
        List<Question> allQuestions = questionRepository.findAll();
        Collections.shuffle(allQuestions);
        List<Question> selectedQuestions = allQuestions.subList(0, Math.min(3, allQuestions.size()));
        room.setQuestions(selectedQuestions);
        room.setStartTime(LocalDateTime.now());
        roomRepository.save(room);
    }

    @Override
    public endGame(Room room) {
        if (room.isGameFinished()) {
            roomRepository.delete(room);
        }
    }

    @Override
    public Room deleteRoom() {

    }

    @Override
    public Room settingRoom(Room room, boolean megiAcceptable, Long ratingLimit){
        room.setMegiAcceptable(megiAcceptable);
        room.setRatingLimit(ratingLimit);
        roomRepository.save(room);
        return room;
    }

}