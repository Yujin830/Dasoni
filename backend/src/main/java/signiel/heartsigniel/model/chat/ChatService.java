package signiel.heartsigniel.model.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.model.chat.dto.ChatRoomDto;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private Map<String, ChatRoomDto> chatRooms;

    //의존관계 주입시 자동실행
    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoomDto> findAllRoom(){
        List<ChatRoomDto> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }


    //채팅방 한개 불러오기

    public ChatRoomDto findById(String roomId){
        return chatRooms.get(roomId);
    }

    //채팅방 생성
    public ChatRoomDto createRoom(String name){
        ChatRoomDto chatRoom = ChatRoomDto.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;


    }



}
