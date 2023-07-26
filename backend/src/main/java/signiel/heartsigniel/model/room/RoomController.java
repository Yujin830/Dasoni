package signiel.heartsigniel.model.room;


import io.swagger.models.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.party.dto.BasicPartyResponse;
import signiel.heartsigniel.model.room.dto.BasicRoomResponse;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final PrivateRoomService privateRoomService;
    private final MatchingRoomService matchingRoomService;

    public RoomController(MemberRepository memberRepository, RoomRepository roomRepository, PrivateRoomService privateRoomService, MatchingRoomService matchingRoomService){
        this.memberRepository = memberRepository;
        this.roomRepository = roomRepository;
        this.privateRoomService = privateRoomService;
        this.matchingRoomService = matchingRoomService;
    }

    @PostMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<String> joinRoom(@PathVariable Long roomId, @PathVariable Long memberId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()-> new RuntimeException("해당 룸을 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new RuntimeException("해당 유저를 찾을 수 없습니다."));

        privateRoomService.joinRoom(member, room);

        //로직 추가

        return ResponseEntity.ok("룸 참가에 성공했습니다.")
    }

    @PostMapping("")
    public ResponseEntity<String> createRoom


}
