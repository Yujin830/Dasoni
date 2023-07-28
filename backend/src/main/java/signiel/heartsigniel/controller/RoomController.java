package signiel.heartsigniel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.room.PrivateRoomService;
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
    public ResponseEntity<Response> joinRoom(@PathVariable Long roomId, @PathVariable Long memberId){
        Response response = privateRoomService.joinRoom(memberId, roomId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{roomId}/members/{memberId}")
    public ResponseEntity<Response> quitRoom(@PathVariable Long roomId, @PathVariable Long memberId){
        Response response = privateRoomService.quitRoom(memberId, roomId);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("")
//    public ResponseEntity<Response> getRoomList()

    @PostMapping("")
    public ResponseEntity<Response> createRoom(@RequestBody PrivateRoomCreate privateRoomCreateRequest) {
        Response response = privateRoomService.createRoom(privateRoomCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Response> getRoomInfo(@PathVariable Long roomId){
        PrivateRoomInfo privateRoomInfo = new PrivateRoomInfo(privateRoomService.findRoomById(roomId));
        return ResponseEntity.ok(Response.of(CommonCode.GOOD_REQUEST, privateRoomInfo));
    }
}
