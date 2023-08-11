package signiel.heartsigniel.model.room;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.matching.code.RoomQueueCode;
import signiel.heartsigniel.model.matching.queue.RatingQueue;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.SpecialMemberMatchingResponse;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.RoomMemberService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class MatchingRoomService {

    private final RoomRepository roomRepository;
    private final LifeService lifeService;
    private final MemberRepository memberRepository;
    private final RoomMemberService roomMemberService;
    private final RedisTemplate<String, Long> redisTemplate;

    public MatchingRoomService(RoomRepository roomRepository, LifeService lifeService, MemberRepository memberRepository, RoomMemberService roomMemberService, RedisTemplate<String, Long> redisTemplate){
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.lifeService = lifeService;
        this.roomMemberService = roomMemberService;
        this.redisTemplate = redisTemplate;
    }

    public Room createRoom(RatingQueue queue) {
        Room room = new Room();

        room.setRoomType("match");
        roomRepository.save(room);
        room.setRatingLimit(queue.getMedianRating());
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

    public void useLifeAndIncreaseMeetingCount(Room room){
        List<RoomMember> roomMemberList = room.getRoomMembers();
        for (RoomMember roomMember : roomMemberList){
            Member member = roomMember.getMember();
            member.setMeetingCount(member.getMeetingCount() + 1);
            memberRepository.save(member);
            lifeService.useLife(member);
        }
    }

    public Response enqueueRoom(Long roomId) {
        // Redis에서 해당 roomId의 요청이 진행 중인지 확인
        Boolean isAbsent = redisTemplate.opsForValue().setIfAbsent("room_request_" + roomId, 0L, 10, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(isAbsent)) {
            // 중복 요청이 아님. 처리 계속 진행
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
            if (room.getRoomType().equals("private")) {
                return Response.of(RoomCode.INVALID_ROOM_TYPE, null);
            }
            String roomQueueName = "ROOM_" + room.getRatingLimit();
            redisTemplate.opsForList().rightPush(roomQueueName, roomId);
            Response response = findSpecialUserAndMatchForRoom(room);
            return response;
        } else {
            // 중복 요청. 처리 거부
            return Response.of(RoomQueueCode.DUPLICATE_REQUEST, null);
        }
    }

    public Response dequeueRoom(Long roomId){
        Room room = findRoomById(roomId);
        String queueName = "ROOM_" + room.getRatingLimit();

        Long removeCount = redisTemplate.opsForList().remove(queueName,1,roomId);
        if(removeCount==0){
            Response response = Response.of(RoomQueueCode.DEQUEUE_FAILED, null);
            return response;
        }
        Response response = Response.of(RoomQueueCode.DEQUEUE_SUCCESS, null);
        return response;
    }

    public Room findRoomForSpecialUser(RatingQueue userQueue) {
        String roomQueueName = "ROOM_" + userQueue.getMedianRating();
        Long roomId = redisTemplate.opsForList().leftPop(roomQueueName);
        if (roomId == null){
            return null;
        }
        Room room = findRoomById(roomId);
        return room;
    }

    public Response findSpecialUserAndMatchForRoom(Room room){
        Long roomLimitRating = room.getRatingLimit();
        RatingQueue maleQueue = RatingQueue.getMegiQueueByMedianRating(roomLimitRating, "male");
        RatingQueue femaleQueue = RatingQueue.getMegiQueueByMedianRating(roomLimitRating, "female");
        Long maleMemberId = (maleQueue != null) ? redisTemplate.opsForList().leftPop(maleQueue.getName()) : null;
        Long femaleMemberId = (femaleQueue != null) ? redisTemplate.opsForList().leftPop(femaleQueue.getName()) : null;
        if (maleMemberId == null || femaleMemberId == null){
            return Response.of(RoomQueueCode.ENQUEUE_SUCCESS, null);
        }
        joinRoom(room, maleMemberId, true);
        joinRoom(room, femaleMemberId, true);
        return Response.of(RoomQueueCode.MATCHING_SUCCESS, SpecialMemberMatchingResponse.of(room.getId()));
    }

    @Transactional
    public void joinRoom(Room room, Long memberId, boolean isSpecialUser){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));
        RoomMember roomMember = roomMemberService.createRoomMember(member, room, isSpecialUser);
        room.getRoomMembers().add(roomMember);
        roomRepository.save(room);
    }

    public Room findRoomById(Long roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
    }

}