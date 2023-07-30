package signiel.heartsigniel.model.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRoomDto {

    private String roomId;
    private String roomName;


    //고유 UUID 번호로 채팅 룸 생성
    public static ChatRoomDto create(String name){
        ChatRoomDto room = new ChatRoomDto();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }
}
