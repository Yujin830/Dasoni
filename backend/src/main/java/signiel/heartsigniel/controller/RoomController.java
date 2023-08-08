package signiel.heartsigniel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.meeting.RatingService;
import signiel.heartsigniel.model.meeting.SignalService;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;

import signiel.heartsigniel.model.room.MatchingRoomService;
import signiel.heartsigniel.model.room.PrivateRoomService;
import signiel.heartsigniel.model.room.code.RoomCode;
import signiel.heartsigniel.model.room.dto.PrivateRoomCreate;
import signiel.heartsigniel.model.room.dto.PrivateRoomList;
import signiel.heartsigniel.model.room.dto.StartRoomRequest;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
public class RoomController {

    private final PrivateRoomService privateRoomService;
    private final MatchingRoomService matchingRoomService;
    private final RatingService ratingService;
    private final SignalService signalService;

    public RoomController(PrivateRoomService privateRoomService, MatchingRoomService matchingRoomService, RatingService ratingService, SignalService signalService) {
        this.privateRoomService = privateRoomService;
        this.matchingRoomService = matchingRoomService;
        this.ratingService = ratingService;
        this.signalService = signalService;
    }

    @PostMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Response> joinRoom(@PathVariable Long roomId, @PathVariable Long memberId) {
        Response response = privateRoomService.joinRoom(memberId, roomId);

        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Response> quitRoom(@PathVariable Long roomId, @PathVariable Long memberId) {
        Response response = privateRoomService.quitRoom(memberId, roomId);
//        privateRoomService.broadcastMemberList(roomId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/{searchKeyword}")
    public ResponseEntity<Response> getRoomByTitle(@PathVariable String searchKeyword) {
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

    @GetMapping("/filter/{condition}")
    public ResponseEntity<Response> filterRoomByGender(@PathVariable String condition) {
        Pageable pageable = PageRequest.of(0, 6);
        Page<PrivateRoomList> roomList = privateRoomService.filterRoomByGender(condition, pageable);
        Response response = Response.of(CommonCode.GOOD_REQUEST, roomList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Response> getRoomInfo(@PathVariable Long roomId) {
        Response response = privateRoomService.roomInfo(roomId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<Response> startMeetingRoom(@PathVariable Long roomId, @RequestBody StartRoomRequest startRoomRequest){
        Long roomLeaderPartyMemberId = startRoomRequest.getRoomLeaderPartyMemberId();
        Response response = privateRoomService.startRoom(roomId, roomLeaderPartyMemberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Response> endMeetingRoom(@RequestBody TotalResultRequest totalResultRequest){

        Response response;

            if (totalResultRequest.getRoomType().equals("match")){
            response = matchingRoomService.endRoom(totalResultRequest);
        } else if (totalResultRequest.getRoomType().equals("private")) {
            response = privateRoomService.endRoom(totalResultRequest);
        } else{
            response = Response.of(RoomCode.NOT_PARTICIPATE_ROOM, null);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roomId}/signals")
    public ResponseEntity<Response> storeSignalInRedis(@PathVariable Long roomId, @RequestBody SingleSignalRequest singleSignalRequest){
        Response response = signalService.storeSignalInRedis(roomId, singleSignalRequest);
        return ResponseEntity.ok(response);
    }


}
