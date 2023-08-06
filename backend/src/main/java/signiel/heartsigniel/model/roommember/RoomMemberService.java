package signiel.heartsigniel.model.roommember;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.roommember.exception.NotFoundRoomMemberException;



@Service
public class RoomMemberService {
    private final RoomMemberRepository roomMemberRepository;

    public RoomMemberService(RoomMemberRepository roomMemberRepository){
        this.roomMemberRepository = roomMemberRepository;
    }

    public RoomMember createRoomMember(Member member){
        RoomMember roomMember = RoomMember.builder()
                .isRoomLeader(false)
                .isSpecialUser(false)
                .member(member)
                .build();

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

}
