package signiel.heartsigniel.model.matching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.common.utils.QueueName;
import signiel.heartsigniel.model.matching.code.MatchingCode;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class MatchingService {
    private final RedisTemplate<String, Member> redisTemplate;
    private final MemberRepository memberRepository;

    public MatchingService(RedisTemplate<String, Member> redisTemplate, MemberRepository memberRepository) {
        this.redisTemplate = redisTemplate;
        this.memberRepository = memberRepository;
    }

    public Response addUserToQueue(Long memberId) {
        Member member = getMemberById(memberId);
        String queueName = getQueueName(member.getGender(), member.getRating());
        ListOperations<String, Member> listOperations = redisTemplate.opsForList();
        listOperations.rightPush(queueName, member);

        //현재 대기열 길이 체크
        Long currentQueueLength = listOperations.size(queueName);


        // 대기열이 3 이상이면 반대 성별 큐 체크
        if (currentQueueLength >= 3) {
            String oppositeGender = member.getGender().equals("male") ? "female" : "male";
            String oppositeQueueName = getQueueName(oppositeGender, member.getRating());

            // 반대 성별 큐의 대기열 길이 체크
            Long oppositeQueueLength = listOperations.size(oppositeQueueName);

            // 반대 성별 큐의 대기열이 3 이상이면 매칭 로직 수행
            if (oppositeQueueLength >= 3) {
                matchUsers(queueName, oppositeQueueName);
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("queueName", queueName);
        data.put("queueSize", currentQueueLength);


        return Response.of(MatchingCode.ENTER_USER_TO_MATCH, null);
    }

    public void matchUsers(String queueName1, String queueName2) {
        ListOperations<String, Member> listOperations = redisTemplate.opsForList();

        // 각 큐에서 3명의 유저를 꺼내와 매칭
        List<Member> usersFromQueue1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            usersFromQueue1.add(listOperations.leftPop(queueName1));
        }

        List<Member> usersFromQueue2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            usersFromQueue2.add(listOperations.leftPop(queueName2));
        }
    }

    private String getQueueName(String gender, Long rating) {
        ListOperations<String, Member> listOperations = redisTemplate.opsForList();

        // 현재 rating 범위 내에 있는 두 개의 큐 선택
        List<QueueName> queueNames = QueueName.getQueueNameByRating(gender, rating);

        // 두 큐의 대기열 길이 확인
        long size1 = listOperations.size(queueNames.get(0).getQueueName());
        long size2 = listOperations.size(queueNames.get(1).getQueueName());

        // 대기열 길이가 같다면, 사용자의 레이팅과 큐의 레이팅 중간값이 가장 가까운 큐 선택
        if (size1 == size2) {
            long midValue1 = queueNames.get(0).getMiddleValue();
            long midValue2 = queueNames.get(1).getMiddleValue();

            long diff1 = Math.abs(midValue1 - rating);
            long diff2 = Math.abs(midValue2 - rating);

            return diff1 < diff2 ? queueNames.get(0).getQueueName() : queueNames.get(1).getQueueName();
        }

        // 그렇지 않다면, 대기열이 더 적은 큐 선택
        return size1 < size2 ? queueNames.get(0).getQueueName() : queueNames.get(1).getQueueName();
    }

    private Member getMemberById(Long memberId) {
        Member memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 유저입니다."));
        return memberEntity;
    }

}
