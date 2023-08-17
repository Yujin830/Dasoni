package signiel.heartsigniel.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.meeting.SignalService;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;

import signiel.heartsigniel.model.room.PrivateRoomService;
import signiel.heartsigniel.model.room.dto.PrivateRoomCreate;
import signiel.heartsigniel.model.room.dto.PrivateRoomList;
import signiel.heartsigniel.model.room.dto.StartRoomRequest;
import signiel.heartsigniel.model.roommember.RoomMemberService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
public class RoomController {

    private final PrivateRoomService privateRoomService;
    private final RoomMemberService roomMemberService;
    private final SignalService signalService;

    public RoomController(PrivateRoomService privateRoomService, SignalService signalService, RoomMemberService roomMemberService) {
        this.privateRoomService = privateRoomService;
        this.signalService = signalService;
        this.roomMemberService = roomMemberService;
    }

    @GetMapping("{roomId}/elapsedTime")
    public ResponseEntity<Long> getElapsedTime(@PathVariable Long roomId){
        LocalDateTime startTime = privateRoomService.getRoomStartTime(roomId);
        long milliSec = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        System.out.println("============ " + startTime);
        System.out.println("============ 밀리세컨즈 " + milliSec);
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        return ResponseEntity.ok(milliSec);
//        return ResponseEntity.ok(duration.getSeconds());
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
    public ResponseEntity<Response> getRoomByTitle(@PathVariable String searchKeyword, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<PrivateRoomList> rooms = privateRoomService.getPrivateRoomsByTitle(searchKeyword, pageable);
        Response response = Response.of(CommonCode.GOOD_REQUEST, rooms);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Response> getRoomList(@RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 6);
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
    public ResponseEntity<Response> filterRoomByGender(@PathVariable String condition, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
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
        Response response = privateRoomService.startRoom(startRoomRequest, roomId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Response> calculateMeetingResult(@PathVariable Long roomId){
        Response response = privateRoomService.endRoom(roomId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roomId}/signals")
    public ResponseEntity<Response> storeSignalInRedis(@PathVariable Long roomId, @RequestBody SingleSignalRequest singleSignalRequest){
        Response response = signalService.storeSignalInRedis(roomId, singleSignalRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{roomId}/members/{memberId}")
    public ResponseEntity<Response> getMemberMeetingResult(@PathVariable Long roomId, @PathVariable Long memberId){
        Response response = roomMemberService.getMeetingResultForRoomMember(roomId, memberId);
        return ResponseEntity.ok(response);
    }


}
