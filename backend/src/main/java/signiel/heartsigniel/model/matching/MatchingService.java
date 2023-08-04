package signiel.heartsigniel.model.matching;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.alarm.AlarmService;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.matching.dto.QuickFindResult;
import signiel.heartsigniel.model.matching.queue.RatingQueue;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.party.Party;
import signiel.heartsigniel.model.party.PartyRepository;
import signiel.heartsigniel.model.party.PartyService;
import signiel.heartsigniel.model.party.dto.PartyMatchResult;
import signiel.heartsigniel.model.party.exception.NoPartyMemberException;
import signiel.heartsigniel.model.partymember.PartyMember;
import signiel.heartsigniel.model.partymember.PartyMemberRepository;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.dto.PrivateRoomInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MatchingService {

    private final MemberRepository memberRepository;
    private final MatchingRoomService matchingRoomService;
    private final PartyService partyService;
    private final PartyRepository partyRepository;
    private final PartyMemberRepository partyMemberRepository;
    private final AlarmService alarmService;
    private final RedisTemplate<String, Long> redisTemplate;

    public MatchingService(MemberRepository memberRepository, MatchingRoomService matchingRoomService, PartyService partyService, PartyRepository partyRepository, PartyMemberRepository partyMemberRepository, AlarmService alarmService, RedisTemplate<String, Long> redisTemplate){
        this.memberRepository = memberRepository;
        this.matchingRoomService = matchingRoomService;
        this.partyService = partyService;
        this.partyRepository = partyRepository;
        this.partyMemberRepository = partyMemberRepository;
        this.alarmService = alarmService;
        this.redisTemplate = redisTemplate;
    }

    public Response quickFindMatch(Long memberId){
        Member memberEntity = findMemberById(memberId);

        // 이미 참가중일 경우 거절메세지 추가
        if (isMemberInAnotherParty(memberEntity)) {
            return Response.of(MatchingCode.ALREADY_IN_MATCHING_QUEUE, null);
        }

        Party party = partyService.findSuitableParties(memberEntity);
        PartyMember partyMember = partyService.joinParty(party, memberEntity);

        // 매칭 안됐을 경우 파티 멤버 번호 응답하기위한 DTO 객체 생성
        QuickFindResult quickFindResult = QuickFindResult.builder()
                .partyMemberId(partyMember.getId())
                .build();

        // 파티 인원이 세명일 경우의 로직
        if (partyService.isPartyFull(party)){

            // 파티 매칭
            Optional<PartyMatchResult> partyMatchResult = partyService.matchParty(party);

            // 매칭 된 경우
            if (partyMatchResult.isPresent()){
                Room matchingRoom = matchingRoomService.createRoom(partyMatchResult.get());

                // 매칭 완료 메시지 전송
                alarmService.sendMatchCompleteMessage(matchingRoom);
                matchingRoomService.startRoom(matchingRoom);

                // 매칭된 파티의 유저들에게 화상채팅방 url 전송
                return Response.of(MatchingCode.MATCHING_SUCCESS, PrivateRoomInfo.of(matchingRoom));
            }

            // 매칭이 안된 경우 - 매칭 대기 메시지 등을 전달
            return Response.of(MatchingCode.MATCHING_PENDING, quickFindResult);
        }
        // 파티 인원이 세명이 아닐 경우
        return Response.of(MatchingCode.MATCHING_STARTED, quickFindResult);
    }

    public Response cancelFindMatch(Long partyMemberId){
        PartyMember partyMember = findPartyMemberById(partyMemberId);
        partyService.quitParty(partyMember);
        return Response.of(MatchingCode.USER_REMOVED_FROM_MATCH, null);
    }

    public void enqueueMember(Member member) {
        RatingQueue queue = RatingQueue.getQueueByRatingAndGender(member.getRating(), member.getGender());
        if (queue != null) {
            redisTemplate.opsForList().rightPush(queue.getName(), member.getMemberId());
            checkAndMatchUsers(queue);
        }
    }

    private void checkAndMatchUsers(RatingQueue queue) {
        if (redisTemplate.opsForList().size(queue.getName()) >= 3) {
            RatingQueue oppositeQueue = RatingQueue.getOppositeGenderQueue(queue);
            if (redisTemplate.opsForList().size(oppositeQueue.getName()) >= 3) {
                // 해당 큐와 상대 성별 큐에서 3명씩 팝해서 매칭
                List<Long> matchedMembersId = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    matchedMembersId.add(redisTemplate.opsForList().leftPop(queue.getName()));
                    matchedMembersId.add(redisTemplate.opsForList().leftPop(oppositeQueue.getName()));
                }
                // 매칭 처리 로직
                // 예: matchingService.match(matchedUsers);
                for (Long memberId : matchedMembersId){
                    System.out.println(memberId);
                }
            }
        }
    }
    public Member findMemberById(Long targetMemberId){
        Member memberEntity =  memberRepository.findById(targetMemberId)
                .orElseThrow(()-> new MemberNotFoundException("해당 유저를 찾지 못하였습니다."));

        return memberEntity;
    }

    public PartyMember findPartyMemberById(Long targetPartyMemberId){
        PartyMember partyMemberEntity = partyMemberRepository.findById(targetPartyMemberId)
                .orElseThrow(() -> new NoPartyMemberException("파티 멤버를 찾지 못했습니다."));

        return partyMemberEntity;
    }

    public boolean isMemberInAnotherParty(Member member) {
        return partyMemberRepository.findPartyMemberByMember(member).isPresent();
    }

}
