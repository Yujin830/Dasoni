package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.question.Question;
import signiel.heartsigniel.model.question.QuestionService;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.MatchingRoomCreated;
import signiel.heartsigniel.model.room.dto.PrivateRoomInfo;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchingRoomService {

    private final RoomRepository roomRepository;

    public MatchingRoomService(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @Transactional
    public Response createRoom(Party maleParty, Party femaleParty) {
        Room room = new Room();

        room.setFemaleParty(femaleParty);
        room.setMaleParty(maleParty);
        room.setRoomType("matching");
        room.setRatingLimit(0L);
        room.setTitle(room.getId() + "번 자동매칭방");
        room.setMegiAcceptable(true);
        room.setStartTime(LocalDateTime.now());
        roomRepository.save(room);

        Response response = Response.of(CommonCode.GOOD_REQUEST, PrivateRoomInfo.of(room));


        return Response.of(CommonCode.GOOD_REQUEST, response);
    }



}
