package signiel.heartsigniel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import signiel.heartsigniel.model.room.Room;
import signiel.heartsigniel.model.room.RoomService;

@RestController
public class RoomController {

    private final RoomService;
    @PostMapping("/room")
    public ResponseEntity<Response>

    @GetMapping("/room")

}
