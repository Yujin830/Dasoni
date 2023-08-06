package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.rating.RatingService;
import signiel.heartsigniel.model.rating.dto.TotalResultRequest;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class MatchingRoomService {

    private final RoomRepository roomRepository;
    private final LifeService lifeService;
    private final PrivateRoomService privateRoomService;
    private final RatingService ratingService;

    public MatchingRoomService(RoomRepository roomRepository, LifeService lifeService, PrivateRoomService privateRoomService, RatingService ratingService){
        this.roomRepository = roomRepository;
        this.lifeService = lifeService;
        this.privateRoomService = privateRoomService;
        this.ratingService = ratingService;
    }

    public Room createRoom() {
        Room room = new Room();

        room.setRoomType("match");

        room.setRatingLimit(0L);
        room.setTitle(room.getId() + "번 자동매칭방");
        room.setMegiAcceptable(true);
        room.setStartTime(LocalDateTime.now());
        return room;
    }

    public void startRoom(Room room){
        room.setStartTime(LocalDateTime.now());
        privateRoomService.useLifeAndIncreaseMeetingCount(room);
        roomRepository.save(room);
    }

    public Response endRoom(TotalResultRequest totalResultRequest){

        Response response = ratingService.calculateTotalResult(totalResultRequest);
        Room roomEntity = findRoomById(totalResultRequest.getRoomId());
        deleteRoomEntity(roomEntity);

        return response;
    }

    public void deleteRoomEntity(Room room){
        roomRepository.delete(room);
    }

    public Room findRoomById(Long roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
        return room;
    }

}