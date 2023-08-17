package signiel.heartsigniel.model.room;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class MatchingRoomService {

    private final RoomRepository roomRepository;
    private final LifeService lifeService;
    private final MemberRepository memberRepository;
    private final RoomMemberService roomMemberService;
    private final RedisTemplate<String, Long> redisTemplate;
    private final AlarmService alarmService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MatchingRoomService(RoomRepository roomRepository, LifeService lifeService, MemberRepository memberRepository, RoomMemberService roomMemberService, RedisTemplate<String, Long> redisTemplate, AlarmService alarmService){
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.lifeService = lifeService;
        this.roomMemberService = roomMemberService;
        this.redisTemplate = redisTemplate;
        this.alarmService = alarmService;
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
        log.info("StartRoom!!!!");
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
        log.info("enqueuRoom called");
        // Redis에서 해당 roomId의 요청이 진행 중인지 확인
        Boolean isAbsent = redisTemplate.opsForValue().setIfAbsent("room_enqueue_request_" + roomId, 0L, 10, TimeUnit.SECONDS);

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

        if (redisTemplate.opsForList().size(maleQueue.getName()) >= 1 && redisTemplate.opsForList().size(femaleQueue.getName()) >= 1 && redisTemplate.opsForList().size("ROOM_"+room.getRatingLimit()) >= 1){
            redisTemplate.opsForList().leftPop("ROOM_"+room.getRatingLimit());
            Long maleMemberId = redisTemplate.opsForList().leftPop(maleQueue.getName());
            Long femaleMemberId = redisTemplate.opsForList().leftPop(femaleQueue.getName());
            joinRoom(room, maleMemberId, true);
            joinRoom(room, femaleMemberId, true);
            return Response.of(RoomQueueCode.MATCHING_SUCCESS, SpecialMemberMatchingResponse.of(room.getId()));
        }
        return Response.of(RoomQueueCode.ENQUEUE_SUCCESS, null);
    }

    @Transactional
    public void joinRoom(Room room, Long memberId, boolean isSpecialUser){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));

        boolean isRoomLeader = false; // 기본값은 false

        // 방에 리더가 없는지 확인
        boolean hasLeader = room.getRoomMembers().stream().anyMatch(RoomMember::isRoomLeader);

        // 방에 리더가 없고, 멤버가 없는 경우 첫 번째 멤버로 판단
        if (!hasLeader && room.getRoomMembers().isEmpty()) {
            isRoomLeader = true;
        }
        if (isSpecialUser){
            lifeService.useLife(member);
        }

        RoomMember roomMember = roomMemberService.createRoomMember(member, room, isSpecialUser, isRoomLeader);
        room.getRoomMembers().add(roomMember);

        // 메기 매칭 메시지 전송 및 생명 감소, 나중에 무조건 리팩토링
        if (isSpecialUser){
            lifeService.useLife(member);
            scheduler.schedule(() -> {
                alarmService.sendMegiMatchCompleteMessage(room, roomMember);
            }, 1000, TimeUnit.MILLISECONDS);
        }
        roomRepository.save(room);
    }

    public Room findRoomById(Long roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을 수 없습니다."));
    }

}