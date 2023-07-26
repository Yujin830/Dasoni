package signiel.heartsigniel.model.room;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.room.dto.PrivateRoomCreate;
import signiel.heartsigniel.model.room.dto.PrivateRoomInfo;

@RestController
@RequestMapping("/rooms")
@Slf4j
public class RoomController {

    private final PrivateRoomService privateRoomService;
    private final MatchingRoomService matchingRoomService;

    public RoomController(PrivateRoomService privateRoomService, MatchingRoomService matchingRoomService){
        this.privateRoomService = privateRoomService;
        this.matchingRoomService = matchingRoomService;
    }

    @PostMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<String> joinRoom(@PathVariable Long roomId, @PathVariable Long memberId){



        return ResponseEntity.ok("룸 참가에 성공했습니다.");
    }

    @GetMapping("")
    public ResponseEntity<Response> getRoomList()

    @PostMapping("")
    public ResponseEntity<String> createRoom(@RequestBody PrivateRoomCreate privateRoomCreateRequest) {
        privateRoomService.createRoom(privateRoomCreateRequest);
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, privateRoomService.))
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Response> getRoomInfo(@PathVariable Long roomId){

        PrivateRoomInfo privateRoomInfo;

        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, privateRoomInfo))
    }

}
