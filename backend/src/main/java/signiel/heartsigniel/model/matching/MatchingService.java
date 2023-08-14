package signiel.heartsigniel.model.matching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.life.code.LifeCode;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.matching.queue.RatingQueue;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.dto.MatchingRoomInfo;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.RoomMemberRepository;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.roommember.RoomMemberService;
import signiel.heartsigniel.model.roommember.exception.NotFoundRoomMemberException;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@Transactional
public class MatchingService {

    private final MemberRepository memberRepository;
    private final MatchingRoomService matchingRoomService;
    private final RoomMemberRepository roomMemberRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final RoomMemberService roomMemberService;
    private AlarmService alarmService;
    private LifeService lifeService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MatchingService(MemberRepository memberRepository, RoomMemberService roomMemberService, RoomMemberRepository roomMemberRepository, MatchingRoomService matchingRoomService, RedisTemplate<String, Long> redisTemplate, AlarmService alarmService, LifeService lifeService){
        this.memberRepository = memberRepository;
        this.matchingRoomService = matchingRoomService;
        this.roomMemberRepository = roomMemberRepository;
        this.roomMemberService = roomMemberService;
        this.redisTemplate = redisTemplate;
        this.alarmService = alarmService;
        this.lifeService = lifeService;
    }

    public Response enqueueMember(Long memberId, String type) {
        Member member = findMemberById(memberId);
        if (isMemberInAnyQueue(memberId)){
            return Response.of(MatchingCode.ALREADY_IN_MATCHING_QUEUE, null);
        }
        if (lifeService.countRemainingLives(memberId) == 0){
            return Response.of(LifeCode.LACK_OF_LIFE, null);
        }
        RatingQueue queue = RatingQueue.getQueueByRatingAndGender(member.getRating(), member.getGender(), type);
        redisTemplate.opsForList().rightPush(queue.getName(), memberId);
        markMemberInQueue(memberId, queue.getName());

        return checkAndMatchUsers(queue, type);
    }

    public Response dequeueMember(Long memberId) {
        if (!isMemberInAnyQueue(memberId)){
            return Response.of(MatchingCode.DEQUEUE_FAIL, null);
        }
        String queueName = getQueueNameForMember(memberId);
        redisTemplate.opsForList().remove(queueName,1,  memberId);
        unmarkMemberFromQueue(memberId);
        return Response.of(MatchingCode.DEQUEUE_SUCCESS, null);
    }

    private Response checkAndMatchUsers(RatingQueue queue, String type) {

        log.info("sizecheck = " + redisTemplate.opsForList().size(queue.getName()));
        // 메기 매칭일 경우
        if(type.equals("special")){
            if (redisTemplate.opsForList().size(queue.getName()) >= 1){
                RatingQueue oppositeQueue = RatingQueue.getOppositeGenderQueue(queue);
                // 상대 큐도 꽉차있을 경우
                if(redisTemplate.opsForList().size(oppositeQueue.getName()) >= 1){
                    Room matchingRoom = matchingRoomService.findRoomForSpecialUser(queue);
                    // 해당 방이 없을 경우
                    if (matchingRoom == null){
                        return Response.of(MatchingCode.MATCHING_PENDING, null);
                    }
                    // 방이 있을 경우 가입
                    Long queueMemberId = redisTemplate.opsForList().leftPop(queue.getName());
                    log.info("queueMemebrId= " + queueMemberId);
                    Long opponentQueueMemberId = redisTemplate.opsForList().leftPop(oppositeQueue.getName());
                    matchingRoomService.joinRoom(matchingRoom, queueMemberId, true);
                    matchingRoomService.joinRoom(matchingRoom,opponentQueueMemberId,true);
                    unmarkMemberFromQueue(queueMemberId);
                    unmarkMemberFromQueue(opponentQueueMemberId);
                    scheduler.schedule(() -> {
                        alarmService.sendMatchCompleteMessage(matchingRoom);
                    }, 1000, TimeUnit.MILLISECONDS);
                    return Response.of(MatchingCode.MATCHING_SUCCESS, MatchingRoomInfo.of(matchingRoom));

                }return Response.of(MatchingCode.MATCHING_PENDING, null);
            }
        }
        if (redisTemplate.opsForList().size(queue.getName()) >= 3) {
            RatingQueue oppositeQueue = RatingQueue.getOppositeGenderQueue(queue);
            log.info("oppositeQueue" + oppositeQueue.toString());
            if (redisTemplate.opsForList().size(oppositeQueue.getName()) >= 3) {
                // 해당 큐와 상대 성별 큐에서 3명씩 팝해서 매칭(createRoom을 향후 Q1, Q2 넣도록 변경)
                Room matchingRoom = matchingRoomService.createRoom(queue);
                for (int i = 0; i < 3; i++) {
                    Long queueMemberId = redisTemplate.opsForList().leftPop(queue.getName());
                    log.info("queuememberId = " + queueMemberId);
                    Long opponentQueueMemberId = redisTemplate.opsForList().leftPop(oppositeQueue.getName());

                    log.info("opponentQueueMemberId = " + queueMemberId);
                    matchingRoomService.joinRoom(matchingRoom, queueMemberId, false);
                    matchingRoomService.joinRoom(matchingRoom, opponentQueueMemberId, false);
                    unmarkMemberFromQueue(queueMemberId);
                    unmarkMemberFromQueue(opponentQueueMemberId);
                }
                matchingRoomService.startRoom(matchingRoom);
                scheduler.schedule(() -> {
                    alarmService.sendMatchCompleteMessage(matchingRoom);
                }, 1000, TimeUnit.MILLISECONDS);
                return Response.of(MatchingCode.MATCHING_SUCCESS, MatchingRoomInfo.of(matchingRoom));
            }
        }
        return Response.of(MatchingCode.MATCHING_STARTED, queue);
    }

    public Member findMemberById(Long targetMemberId){
        Member memberEntity =  memberRepository.findById(targetMemberId)
                .orElseThrow(()-> new MemberNotFoundException("해당 유저를 찾지 못하였습니다."));

        return memberEntity;
    }

    public RoomMember findRoomMemberById(Long targetRoomMemberId){
        RoomMember roomMemberEntity = roomMemberRepository.findById(targetRoomMemberId)
                .orElseThrow(() -> new NotFoundRoomMemberException("해당 룸멤버를 찾지 못했습니다."));

        return roomMemberEntity;
    }

    // 대기열에 유저 추가
    public void markMemberInQueue(Long memberId, String queueName) {
        String key = "memberQueueStatus";
        redisTemplate.opsForHash().put(key, memberId.toString(), queueName);
    }

    // 대기열에서 유저 제거
    public void unmarkMemberFromQueue(Long memberId) {
        String key = "memberQueueStatus";
        redisTemplate.opsForHash().delete(key, memberId.toString());
    }

    // 유저가 어떤 대기열에 있는지 확인
    public String getQueueNameForMember(Long memberId) {
        String key = "memberQueueStatus";
        return (String) redisTemplate.opsForHash().get(key, memberId.toString());
    }

    // 유저가 어떤 대기열에 있는지 확인 (boolean 반환)
    public boolean isMemberInAnyQueue(Long memberId) {
        String key = "memberQueueStatus";
        return redisTemplate.opsForHash().hasKey(key, memberId.toString());
    }

}
