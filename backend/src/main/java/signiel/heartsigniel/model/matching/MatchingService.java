package signiel.heartsigniel.model.matching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.life.code.LifeCode;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.matching.dto.QueueData;
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

@Service
@Transactional
public class MatchingService {

    private final MemberRepository memberRepository;
    private final MatchingRoomService matchingRoomService;
    private final RoomMemberRepository roomMemberRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private final RoomMemberService roomMemberService;
    private AlarmService alarmService;
    private LifeService lifeService;

    public MatchingService(MemberRepository memberRepository, RoomMemberService roomMemberService, RoomMemberRepository roomMemberRepository, MatchingRoomService matchingRoomService, RedisTemplate<String, Long> redisTemplate, AlarmService alarmService, LifeService lifeService){
        this.memberRepository = memberRepository;
        this.matchingRoomService = matchingRoomService;
        this.roomMemberRepository = roomMemberRepository;
        this.roomMemberService = roomMemberService;
        this.redisTemplate = redisTemplate;
        this.alarmService = alarmService;
        this.lifeService = lifeService;
    }

    public Response enqueueMember(Long memberId) {
        Member member = findMemberById(memberId);
        if (isMemberMarkedInQueue(memberId)){
            return Response.of(MatchingCode.ALREADY_IN_MATCHING_QUEUE, null);
        }
        if (lifeService.countRemainingLives(memberId) == 0){
            return Response.of(LifeCode.LACK_OF_LIFE, null);
        }
        RatingQueue queue = RatingQueue.getQueueByRatingAndGender(member.getRating(), member.getGender());
        redisTemplate.opsForList().rightPush(queue.getName(), member.getMemberId());
        markMembersInQueue(memberId);

        return checkAndMatchUsers(queue);
    }

    public Response dequeueMember(QueueData queueData) {
        redisTemplate.opsForList().remove(queueData.getQueue(),1,  queueData.getMemberId());
        unmarkMemberAsInQueue(queueData.getMemberId());
        return Response.of(MatchingCode.DEQUEUE_SUCCESS, null);
    }

    private Response checkAndMatchUsers(RatingQueue queue) {
        if (redisTemplate.opsForList().size(queue.getName()) >= 3) {
            RatingQueue oppositeQueue = RatingQueue.getOppositeGenderQueue(queue);
            if (redisTemplate.opsForList().size(oppositeQueue.getName()) >= 3) {
                // 해당 큐와 상대 성별 큐에서 3명씩 팝해서 매칭(createRoom을 향후 Q1, Q2 넣도록 변경)
                Room matchingRoom = matchingRoomService.createRoom();
                for (int i = 0; i < 3; i++) {
                    Long queueMemberId = redisTemplate.opsForList().leftPop(queue.getName());
                    Long opponentQueueMemberId = redisTemplate.opsForList().leftPop(oppositeQueue.getName());
                    matchingRoom.getRoomMembers().add(roomMemberService.createRoomMember(findMemberById(queueMemberId), matchingRoom));
                    matchingRoom.getRoomMembers().add(roomMemberService.createRoomMember(findMemberById(opponentQueueMemberId), matchingRoom));
                    unmarkMemberAsInQueue(queueMemberId);
                    unmarkMemberAsInQueue(opponentQueueMemberId);
                }
                matchingRoomService.startRoom(matchingRoom);
                alarmService.sendMatchCompleteMessage(matchingRoom);
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

    // 대기열에 참가한 유저를 대기열 참가여부 set에도 추가
    public void markMembersInQueue(Long memberId){
        String key = "member:inQueue";
        redisTemplate.opsForSet().add(key, memberId);
    }

    // 대기열 취소하거나 매칭 되었을 경우
    public void unmarkMemberAsInQueue(Long memberId) {
        String key = "member:inQueue";
        redisTemplate.opsForSet().remove(key, memberId.toString());
    }

    public boolean isMemberMarkedInQueue(Long memberId) {
        String key = "member:inQueue";
        return redisTemplate.opsForSet().isMember(key, memberId);
    }


}
