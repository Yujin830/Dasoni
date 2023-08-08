package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.meeting.RatingService;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;
import signiel.heartsigniel.model.roommember.RoomMember;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MatchingRoomService {

    private final RoomRepository roomRepository;
    private final RatingService ratingService;
    private final LifeService lifeService;
    private final MemberRepository memberRepository;

    public MatchingRoomService(RoomRepository roomRepository, RatingService ratingService, LifeService lifeService, MemberRepository memberRepository){
        this.roomRepository = roomRepository;
        this.ratingService = ratingService;
        this.memberRepository = memberRepository;
        this.lifeService = lifeService;
    }

    public Room createRoom() {
        Room room = new Room();

        room.setRoomType("match");
        roomRepository.save(room);
        room.setRatingLimit(0L);
        room.setTitle(room.getId() + "번 자동매칭방");
        room.setMegiAcceptable(true);
        room.setStartTime(LocalDateTime.now());
        room.setRoomMembers(new ArrayList<>());
        return room;
    }

    public void startRoom(Room room){
        room.setStartTime(LocalDateTime.now());
        useLifeAndIncreaseMeetingCount(room);
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

    public void useLifeAndIncreaseMeetingCount(Room room){
        List<RoomMember> roomMemberList = room.getRoomMembers();
        for (RoomMember roomMember : roomMemberList){
            Member member = roomMember.getMember();
            member.setMeetingCount(member.getMeetingCount() + 1);
            memberRepository.save(member);
            lifeService.useLife(member);
        }
    }

}