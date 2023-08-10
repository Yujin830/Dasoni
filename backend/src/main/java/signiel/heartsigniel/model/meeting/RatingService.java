package signiel.heartsigniel.model.meeting;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.exception.MemberNotFoundException;
import signiel.heartsigniel.model.meeting.dto.*;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.RoomMemberRepository;
import signiel.heartsigniel.model.meeting.code.RatingCode;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.RoomRepository;

import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import java.util.*;

@Service
public class RatingService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RedisTemplate<String, SignalMatchingResult> redisTemplate;
    private final SignalService signalService;
    private static final int K_FACTOR = 40;
    private int defaultMemberSize = 6;

    public RatingService(RoomRepository roomRepository, MemberRepository memberRepository, RoomMemberRepository roomMemberRepository, RedisTemplate<String, SignalMatchingResult> redisTemplate, SignalService signalService) {
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.roomMemberRepository = roomMemberRepository;
        this.redisTemplate = redisTemplate;
        this.signalService = signalService;
    }

    public Response calculateTotalResult(Long roomId) {
        Room room = findRoomById(roomId);
        List<RoomMember> roomMembers = room.getRoomMembers();
        int roomMemberSize = roomMembers.size();

        List<SingleSignalRequest> singleSignalRequests = signalService.fetchAllSignalsForRoom(roomId);

        Map<Integer, Integer> roomMemberSequence = makeRoomMemberSequence(roomMembers);

        List<int[][]> signalBoards = makeSignalBoard(roomMemberSize, singleSignalRequests, roomMemberSequence);
        int[] scoreBoard = calculatePersonalScore(singleSignalRequests, roomMembers, signalBoards);

        List<RoomMember> sortedRoomMembers = sortRoomMembersByScore(roomMembers, scoreBoard);
        int[][] finalBoard = signalBoards.get(1);
        int[] mutuallySignaledList = isMutuallySignaled(finalBoard, roomMemberSequence);

        Long avgRating = calculateAvgRatingOfRoom(room);
        String roomType = room.getRoomType();

        for (int i = 0; i < roomMemberSize; i++) {
            if (mutuallySignaledList[i] != 0) {
                addMatchedMemberId((long) i, (long) mutuallySignaledList[i]);
            }
        }

        List<PersonalResult> personalResults = new ArrayList<>();
        for (int rank = 1; rank <= roomMemberSize; rank++) {
            RoomMember roomMember = sortedRoomMembers.get(rank - 1);
            Member member = roomMember.getMember();
            Long memberId = member.getMemberId();
            Long ratingChange = 0L;
            if (roomType.equals("match")) {
                Long myRating = member.getRating();
                ratingChange = calculateRatingChange(myRating, avgRating, rank, K_FACTOR, roomMemberSize);
                saveMemberRating(member, myRating + ratingChange);
            }
            int signalOpponent = mutuallySignaledList[roomMemberSequence.get(memberId.intValue())];
            if (signalOpponent != 0) {
                addMatchedMemberId(memberId, (long) signalOpponent);
            }
            if (roomMember.isSpecialUser()) {
                ratingChange *= 2;
            }
            PersonalResult result = PersonalResult.of(roomMember, ratingChange, signalOpponent);
            personalResults.add(result);
        }

        TotalResultResponse totalResultResponse = TotalResultResponse.of(personalResults, roomId);
        signalService.deleteAllSignalsForRoom(roomId);
        return Response.of(RatingCode.CALCULATION_SUCCESS, totalResultResponse);
    }

    public Long calculateRatingChange(Long memberRating, Long avgRating, int rank, int kFactor, int roomMemberSize) {
        // 순위에 따른 기본 점수 변동 배열 (인덱스 0은 사용하지 않음)
        int[] baseScoreChanges;

        if(roomMemberSize == 6){
            baseScoreChanges = new int[] {0, 47, 30, 15, -15, -30, -47};
        } else {
            baseScoreChanges = new int[] {0, 53, 40, 25,  15, -15, -25, -40, -53};
        }

        // 순위에 맞는 기본 점수 변동 값을 가져옴
        // 만약 순위가 범위 밖이라면 0을 반환
        int baseScoreChange = rank >= 1 && rank <= roomMemberSize ? baseScoreChanges[rank] : 0;

        // Elo 점수계산
        double expectedWinProbability = 1.0 / (1.0 + Math.pow(10, (avgRating - memberRating) / 400.0));
        Long eloChange = Math.round(kFactor * (1 - expectedWinProbability));

        // Elo 점수 변동을 -10에서 +10 사이로 제한
        eloChange = Math.max(-10, Math.min(eloChange, 10));

        // 최종 레이팅 변동 = Elo 점수 변동 + 기본 점수 변동
        Long ratingChange = eloChange + baseScoreChange;

        return ratingChange;
    }


    public Long calculateAvgRatingOfRoom(Room targetRoom) {
        return (targetRoom.memberAvgRatingByGender("male") + targetRoom.memberAvgRatingByGender("female")) / targetRoom.roomMemberCount();
    }

    public List<RoomMember> sortRoomMembersByScore(List<RoomMember> roomMembers, int[] scoreBoard) {
        for (int i = 0; i < roomMembers.size(); i++) {
            RoomMember roomMember = roomMembers.get(i);
            savePartyMemberScore(roomMember, scoreBoard[i]); // PartyMember 객체에 점수 설정
        }
        roomMembers.sort(Comparator.comparingInt(RoomMember::getScore).reversed()); // 점수를 기준으로 내림차순 정렬

        return roomMembers;
    }

    public Map<Integer, Integer> makeRoomMemberSequence(List<RoomMember> roomMembers){
        Map<Integer, Integer> roomMemberSequence = new HashMap<>();
        for (int i = 0; i < roomMembers.size(); i++) {
            roomMemberSequence.put(roomMembers.get(i).getMember().getMemberId().intValue(), i);
        }
        return roomMemberSequence;
    }


    public int[] calculatePersonalScore(List<SingleSignalRequest> singleSignalRequests, List<RoomMember> roomMembers, List<int[][]> signalBoards) {

        // 3:3, 4:4 구분
        int roomMemberSize = roomMembers.size();

        // 최종 스코어를 위한 배열
        int[] finalScore = new int[roomMembers.size()];

        // 시그널 보드 점수로 환산해서 최종 점수에 더하기
        for (int i = 0; i < signalBoards.size(); i++) {
            int[] scoreBoard = calculateSignalScore(signalBoards.get(i), i+1);
            for (int j = 0; j < roomMemberSize; j++) {
                finalScore[i] = scoreBoard[i] + finalScore[i];
            }
        }
        return finalScore;
    }

    public int[] calculateSignalScore(int[][] signalBoard, int signalSequence) {

        int memberSize = signalBoard.length;
        int[] scoreBoard = new int[memberSize];

        // 시그널 횟수 표시
        for (int i = 0; i < memberSize; i++) {
            for (int j = 0; j < memberSize; j++) {
                if (i == j) {
                    continue;
                }
                if (signalBoard[i][j] == 1) {
                    scoreBoard[j] += 1;
                }
            }
        }

        // 첫 시그널 교환일 경우
        if (signalSequence == 1) {
            for (int i = 0; i < memberSize; i++) {
                if (scoreBoard[i] == 3) {
                    scoreBoard[i] = 25;
                } else {
                    scoreBoard[i] = scoreBoard[i] * 5;
                }
            }
        }
        // 최종 선택일 경우
        else {
            for (int i = 0; i < memberSize; i++) {
                if (scoreBoard[i] == 3) {
                    scoreBoard[i] = 50;
                } else {
                    scoreBoard[i] = scoreBoard[i] * 10;
                }
                for (int j = 0; j < memberSize; j++) {
                    if (signalBoard[i][j] == 1 && signalBoard[j][i] == 1) {
                        scoreBoard[i] += 50;
                    }
                }
            }
        }
        return scoreBoard;
    }

    public Room findRoomById(Long roomId) {
        Room roomEntity = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을수 없습니다."));

        return roomEntity;
    }

    public RoomMember savePartyMemberScore(RoomMember roomMember, int score) {
        roomMember.setScore(score);
        return roomMemberRepository.save(roomMember);
    }

    public Member saveMemberRating(Member member, Long rating) {
        member.setRating(rating);
        return memberRepository.save(member);
    }

    public List<int[][]> makeSignalBoard(int roomMemberSize, List<SingleSignalRequest> singleSignalRequests, Map<Integer, Integer> roomMemberSequence){
        List<int[][]> signalBoardList = new ArrayList<>();

        int[][] firstBoard = new int[roomMemberSize][roomMemberSize];
        int[][] finalBoard = new int[roomMemberSize][roomMemberSize];

        for(SingleSignalRequest singleSignalRequest : singleSignalRequests){
            int senderIndex = roomMemberSequence.get(singleSignalRequest.getSenderId());
            int receiverIndex = roomMemberSequence.get(singleSignalRequest.getReceiverId());

            // 첫인상 투표인 경우
            if(singleSignalRequest.getSignalSequence() == 1){
                firstBoard[senderIndex][receiverIndex] = 1;
            }
            // 최종 투표인 경우
            else{
                finalBoard[senderIndex][receiverIndex] = 1;
            }
        }

        signalBoardList.add(firstBoard);
        signalBoardList.add(finalBoard);

        return signalBoardList;
    }

    public String getMemberProfileImage (Long memberId){
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException("해당 유저를 찾을 수 없습니다."));
            return member.getProfileImageSrc();
        }

    public int[] isMutuallySignaled (int[][] signalBoard, Map<Integer, Integer>roomMemberSequence){
        int memberSize = signalBoard.length;
        int[] matchBoard = new int[memberSize]; // 인원수 + 1 만큼의
        for (int i = 0; i < memberSize; i++) {
            for (int j = 0; j < memberSize; j++) {
                if (signalBoard[i][j] == 1 && signalBoard[j][i] == 1) {
                    matchBoard[i] = roomMemberSequence.get(i);
                }
            }
        }
        return matchBoard;
    }

        // 매칭된 유저 추가
    public void addMatchedMemberId(Long memberId, Long matchedUserId){
        SignalMatchingResult signalMatchingResult = SignalMatchingResult.of(memberId, matchedUserId, getMemberProfileImage(matchedUserId));
        redisTemplate.opsForList().rightPush("member:" + memberId + ":matchHistory", signalMatchingResult);

        // 큐 크기 유지
        while (redisTemplate.opsForList().size("member:" + memberId + ":matchHistory") > defaultMemberSize) {
            redisTemplate.opsForList().leftPop("member" + memberId + ":matchHistory");
        }
    }

    // 매칭 히스토리 조회
    public List<SignalMatchingResult> getMatchedMemberIds (Long memberId){
        return redisTemplate.opsForList().range("member:" + memberId + ":matchHistory", 0, -1);
    }

    // 매칭 히스토리 삭제
    public void deleteMatchingHistory (Long memberId){
        redisTemplate.delete("member:" + memberId + ":matchHistory");
    }

    public int getIndexOfId(int id, Map<Integer, Integer> roomMemberSequence) {
        return roomMemberSequence.getOrDefault(id, -1);
    }
}