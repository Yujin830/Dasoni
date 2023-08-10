package signiel.heartsigniel.model.roommember;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.meeting.RatingService;
import signiel.heartsigniel.model.meeting.dto.PersonalResult;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.RoomRepository;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;
import signiel.heartsigniel.model.roommember.code.RoomMemberCode;
import signiel.heartsigniel.model.roommember.exception.NotFoundRoomMemberException;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;


@Service
@Transactional
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;
    private final RatingService ratingService;
    private final RoomRepository roomRepository;

    public RoomMemberService(RoomMemberRepository roomMemberRepository,RatingService ratingService, RoomRepository roomRepository){
        this.roomMemberRepository = roomMemberRepository;
        this.ratingService = ratingService;
        this.roomRepository = roomRepository;
    }

    public RoomMember createRoomMember(Member member, Room room){
        RoomMember roomMember = RoomMember.of(member, room);
        return roomMemberRepository.save(roomMember);
    }

    public RoomMember findRoomMemberByRoomIdAndMemberId(Long roomId, Long memberId){
        RoomMember roomMember = roomMemberRepository.findRoomMemberByRoom_IdAndMember_MemberId(roomId, memberId)
                .orElseThrow(() -> new NotFoundRoomMemberException("해당 유저를 찾을 수 없습니다."));

        return roomMember;
    }

    public void assignRoomLeader(RoomMember roomMember){
        roomMember.setRoomLeader(true);
        roomMemberRepository.save(roomMember);
    }
    public void quitRoom(RoomMember roomMember){
        roomMemberRepository.delete(roomMember);
    }

    public Response getMeetingResultForRoomMember(Long roomId, Long memberId){
        // 방의 시작 시간 조회
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
        LocalDateTime startTime = room.getStartTime();
        LocalDateTime now = LocalDateTime.now();

        // 현재 시간과 시작 시간의 차이 계산
        Duration duration = Duration.between(startTime, now);

        // 50분 미만이면 중도 퇴장에 대한 응답 반환
        if (duration.toMinutes() < 50) {
            return Response.of(RoomCode.MIDWAY_EXIT, null);
        }

        // 그렇지 않으면 개인 결과 조회
        PersonalResult personalResult = ratingService.getAndDeletePersonalResultFromRedis(memberId, roomId);
        return Response.of(RoomMemberCode.FETCH_MEETING_RESULT_SUCCESS, personalResult);
    }
}
