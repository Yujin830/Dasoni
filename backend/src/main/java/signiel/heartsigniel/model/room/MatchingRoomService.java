package signiel.heartsigniel.model.room;

import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.life.LifeService;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.roommember.RoomMember;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MatchingRoomService {

    private final RoomRepository roomRepository;
    private final LifeService lifeService;
    private final MemberRepository memberRepository;

    public MatchingRoomService(RoomRepository roomRepository, LifeService lifeService, MemberRepository memberRepository){
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.lifeService = lifeService;
    }

    public Room createRoom() {
        Room room = new Room();

        room.setRoomType("match");
        roomRepository.save(room);
        room.setRatingLimit(0L);
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

    public Response joinRoomAsSpecialUser(Long roomId, Long memberId){

    }

}