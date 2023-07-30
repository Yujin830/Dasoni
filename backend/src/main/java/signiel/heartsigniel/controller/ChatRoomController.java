package signiel.heartsigniel.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import signiel.heartsigniel.model.chat.ChatService;
import signiel.heartsigniel.model.chat.dto.ChatRoomDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    //채팅 리스트를 화면에 리턴
    @GetMapping("/room")
    public String rooms(Model model){
        return "/chat/chatroom";
    }

    // 모든 채팅방 목록을 반환
    @GetMapping("/rooms")
    public List<ChatRoomDto> getAllRooms(){
        return chatService.findAllRoom();
    }

    //채팅방 개설
    @PostMapping("/room")
    public ChatRoomDto createRoom(@RequestParam String name){
        return chatService.createRoom(name);
    }

    //채팅방 입장 화면 리턴

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId){
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    //특정 채팅방을 조회하여 반환
    @GetMapping("/room/{roomId}")
    public ChatRoomDto roomInfo(@PathVariable String roomId){
        return chatService.findById(roomId);
    }
}
