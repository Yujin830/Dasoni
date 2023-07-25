package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionRepository;
import signiel.heartsigniel.model.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final QuestionRepository questionRepository;

    public RoomService(RoomRepository roomRepository, QuestionRepository questionRepository) {

        this.questionRepository = questionRepository;
        this.roomRepository = roomRepository;
    }


    public Room createRoom(Party maleParty, Party femaleParty) {
        Room room = new Room();
        room.setMaleParty(maleParty);
        room.setFemaleParty(femaleParty);
        roomRepository.save(room);
        return room;
    }

    public void startGame(Room room) {
        List<Question> allQuestions = questionRepository.findAll();
        Collections.shuffle(allQuestions);
        List<Question> selectedQuestions = allQuestions.subList(0, Math.min(3, allQuestions.size()));
        room.setQuestions(selectedQuestions);
        room.setStartTime(LocalDateTime.now());
        roomRepository.save(room);
    }

}