package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionService;
import signiel.heartsigniel.model.room.dto.MatchingRoomCreated;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchingRoomService {

    private final QuestionService questionService;
    private final RoomRepository roomRepository;

    public MatchingRoomService(QuestionService questionService, RoomRepository roomRepository){
        this.questionService = questionService;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Room createRoom(Party maleParty, Party femaleParty) {
        Room room = new Room();

        room.setFemaleParty(femaleParty);
        room.setMaleParty(maleParty);
        room.setType("matching");
        room = roomRepository.save(room);

        room.setRatingLimit(0L);
        room.setTitle(room.getId() + "번 자동매칭방");
        room.setMegiAcceptable(true);
        room.setStartTime(LocalDateTime.now());

        return roomRepository.save(room);
    }



    public List<Question> selectQuestionSet(){
        List<Question> questions = questionService.getRandomQuestions(3);

        return questions;
    }
}
