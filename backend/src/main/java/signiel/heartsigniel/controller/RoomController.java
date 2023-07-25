package signiel.heartsigniel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.room.PrivateRoomService;

@RestController
public class RoomController {

    private final PrivateRoomService;

    @PostMapping("/room")
    public ResponseEntity<Response>

    @GetMapping("/room")

}
