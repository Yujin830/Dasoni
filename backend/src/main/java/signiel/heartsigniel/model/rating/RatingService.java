package signiel.heartsigniel.model.rating;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.roommember.RoomMember;
import signiel.heartsigniel.model.roommember.RoomMemberRepository;
import signiel.heartsigniel.model.rating.code.RatingCode;
import signiel.heartsigniel.model.rating.dto.PersonalResult;
import signiel.heartsigniel.model.rating.dto.SignalResultRequest;
import signiel.heartsigniel.model.rating.dto.TotalResultRequest;
import signiel.heartsigniel.model.rating.dto.TotalResultResponse;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.RoomRepository;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.exception.NotFoundRoomException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RatingService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private static final int K_FACTOR = 40;

    public RatingService(RoomRepository roomRepository, MemberRepository memberRepository, RoomMemberRepository roomMemberRepository){
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.roomMemberRepository = roomMemberRepository;
    }

    public Response calculateTotalResult(TotalResultRequest totalResultRequest){

        List<RoomMember> sortedRoomMembers = sortRoomMembersByScore(totalResultRequest);
        Room room = findRoomById(totalResultRequest.getRoomId());
        Long avgRating = calculateAvgRatingOfRoom(room);

        if (room.getRoomType().equals("private")){
            return Response.of(RoomCode.END_MEETING_SUCCESSFUL, null);
        }

        List<PersonalResult> personalResults = new ArrayList<>();

        for(int rank = 1; rank <= sortedRoomMembers.size(); rank++){
            RoomMember roomMember = sortedRoomMembers.get(rank-1);
            Member member = roomMember.getMember();
            Long myRating = member.getRating();
            Long ratingChange = calculateRatingChange(myRating, avgRating, rank, K_FACTOR);

            //레이팅 적용
            saveMemberRating(member, myRating + ratingChange);

            PersonalResult result = PersonalResult.of(roomMember, ratingChange);
            personalResults.add(result);
        }
        TotalResultResponse totalResultResponse = TotalResultResponse.of(personalResults, totalResultRequest.getRoomId());
        return Response.of(RatingCode.CALCULATION_SUCCESS, totalResultResponse);
    }

    public Long calculateRatingChange(Long myRating, Long avgRating, int rank, int kFactor) {
        // 순위에 따른 기본 점수 변동 배열 (인덱스 0은 사용하지 않음)
        int[] baseScoreChanges = {0, 47, 30, 15, -15, -30, -47};

        // 순위에 맞는 기본 점수 변동 값을 가져옴
        // 만약 순위가 범위 밖이라면 0을 반환
        int baseScoreChange = rank >= 1 && rank <= 6 ? baseScoreChanges[rank] : 0;

        // Elo 점수계산
        double expectedWinProbability = 1.0 / (1.0 + Math.pow(10, (avgRating - myRating) / 400.0));
        Long eloChange = Math.round(kFactor * (1 - expectedWinProbability));

        // Elo 점수 변동을 -10에서 +10 사이로 제한
        eloChange = Math.max(-10, Math.min(eloChange, 10));

        // 최종 레이팅 변동 = Elo 점수 변동 + 기본 점수 변동
        Long ratingChange = eloChange + baseScoreChange;

        return ratingChange;
    }


    public Long calculateAvgRatingOfRoom(Room targetRoom){
        return (targetRoom.memberAvgRatingByGender("male") + targetRoom.memberAvgRatingByGender("female")) / targetRoom.roomMemberCount();
    }

    public List<RoomMember> sortRoomMembersByScore(TotalResultRequest totalResult) {
        Room room = findRoomById(totalResult.getRoomId());
        List<RoomMember> roomMembers = room.getRoomMembers();

        int[] scoreBoard = calculatePersonalScore(totalResult.getSignalResults());
        for (int i = 0; i < 6; i++) {
            RoomMember roomMember = roomMembers.get(i);
            savePartyMemberScore(roomMember, scoreBoard[i]); // PartyMember 객체에 점수 설정
        }

        roomMembers.sort(Comparator.comparingInt(RoomMember::getScore).reversed()); // 점수를 기준으로 내림차순 정렬

        return roomMembers;
    }

    public int[] calculatePersonalScore(List<SignalResultRequest> signalResults){
        int[] finalScore = new int[6];
        for(SignalResultRequest signalResultRequest : signalResults){
            int[] scoreBoard = calculateSignalScore(signalResultRequest);
            for (int i = 0; i < 6; i++) {
                finalScore[i] = scoreBoard[i] + finalScore[i];
            }
        }
        return finalScore;
    }

    public int[] calculateSignalScore(SignalResultRequest signalResult){
        int[] scoreBoard = new int[6];
        int[][] signalBoard = signalResult.getSignalBoard();

        // 시그널 횟수 표시
        for(int i = 0; i < 6; i++){
            for(int j = 0; j<6; j++){
                if (i==j){
                    continue;
                }
                if(signalBoard[i][j] == 1){
                    scoreBoard[j] += 1;
                }
            }
        }

        // 첫 시그널 교환일 경우
        if (signalResult.getSignalType().equals("first")){
            for(int i=0; i<6; i++){
                if (scoreBoard[i] == 3){
                    scoreBoard[i] = 25;
                }else {
                    scoreBoard[i] = scoreBoard[i] * 5;
                }
            }
        }
        // 최종 선택일 경우
        else{
            for(int i=0; i<6; i++){
                if (scoreBoard[i] == 3){
                    scoreBoard[i] = 50;
                }else {
                    scoreBoard[i] = scoreBoard[i] * 10;
                }
                for (int j=0; j<6; j++){
                    if(signalBoard[i][j] == 1 && signalBoard[j][i] == 1){
                        scoreBoard[i] += 50;
                    }
                }
            }
        }
        return scoreBoard;
    }

    public Room findRoomById(Long roomId){
        Room roomEntity = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundRoomException("해당 방을 찾을수 없습니다."));

        return roomEntity;
    }

    public RoomMember savePartyMemberScore(RoomMember roomMember, int score){
        roomMember.setScore(score);
        return roomMemberRepository.save(roomMember);
    }

    public Member saveMemberRating(Member member, Long rating){
        member.setRating(rating);
        return memberRepository.save(member);
    }

}