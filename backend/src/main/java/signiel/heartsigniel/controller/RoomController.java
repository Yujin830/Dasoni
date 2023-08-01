package signiel.heartsigniel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.WebSocketInfo;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.room.PrivateRoomService;
import signiel.heartsigniel.model.room.dto.PrivateRoomCreate;
import signiel.heartsigniel.model.room.dto.PrivateRoomList;

@RestController
@RequestMapping("/rooms")
@Slf4j
public class RoomController {

    private final PrivateRoomService privateRoomService;
    private final MatchingRoomService matchingRoomService;
    private final ChatService chatService;

    public RoomController(PrivateRoomService privateRoomService, MatchingRoomService matchingRoomService, ChatService chatService){
        this.privateRoomService = privateRoomService;
        this.matchingRoomService = matchingRoomService;
        this.chatService = chatService;
    }

    @PostMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Response> joinRoom(@PathVariable Long roomId, @PathVariable Long memberId){
        Response response = privateRoomService.joinRoom(memberId, roomId);
        response.setWebSocketInfo(new WebSocketInfo("/ws/chat", roomId));


        Member loggedInMember = chatService.getLoggedInMember();

        if(loggedInMember != null){
            response.setMember(loggedInMember);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Response> quitRoom(@PathVariable Long roomId, @PathVariable Long memberId){
        Response response = privateRoomService.quitRoom(memberId, roomId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{searchKeyword}")
    public ResponseEntity<Response> getRoomByTitle(@PathVariable String searchKeyword)
    {
        Pageable pageable = PageRequest.of(0, 6);
        Page<PrivateRoomList> rooms = privateRoomService.getPrivateRoomsByTitle(searchKeyword, pageable);
        Response response = Response.of(CommonCode.GOOD_REQUEST, rooms);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response> getRoomList() {

        Pageable pageable = PageRequest.of(0, 6);
        Page<PrivateRoomList> rooms = privateRoomService.getPrivateRooms(pageable);
        Response response = Response.of(CommonCode.GOOD_REQUEST, rooms);

        return ResponseEntity.ok(response);
        }

    @PostMapping("")
    public ResponseEntity<Response> createRoom(@RequestBody PrivateRoomCreate privateRoomCreateRequest) {
        Response response = privateRoomService.createRoom(privateRoomCreateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{gender}")
    public ResponseEntity<Response> filterRoomByGender(@PathVariable String gender){
        Pageable pageable = PageRequest.of(0 ,6);
        Page<PrivateRoomList> roomList = privateRoomService.filterRoomByGender(gender, pageable);
        Response response = Response.of(CommonCode.GOOD_REQUEST, roomList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Response> getRoomInfo(@PathVariable Long roomId){
        Response response = privateRoomService.roomInfo(roomId);
        return ResponseEntity.ok(response);
    }
}
