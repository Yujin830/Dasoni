package signiel.heartsigniel.model.chat;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import signiel.heartsigniel.common.code.CommonCode;
import signiel.heartsigniel.common.dto.Response;
import signiel.heartsigniel.model.chat.dto.ChatRoom;
import signiel.heartsigniel.model.member.Member;
import signiel.heartsigniel.model.member.MemberRepository;
import signiel.heartsigniel.model.member.dto.MeetingRoomMemberReq;
import signiel.heartsigniel.model.room.dto.PrivateRoomInfo;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    private Map<String, ChatRoom> chatRooms;

    //의존관계 주입시 자동실행
    @PostConstruct
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }


    public Member getLoggedInMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            String userName = authentication.getName();
            Member member = memberRepository.findByLoginId(userName).get();

            return member;
        }

        return null;
    }
    //채팅방 불러오기
    public List<ChatRoom> findAllRoom(){
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    public String createChattingRoomUrl(Long roomId){
        return "/ws/chat/" + roomId;
    }

    //채팅방 한개 불러오기

    public ChatRoom findById(String roomId){
        return chatRooms.get(roomId);
    }

    //채팅방 생성
    public ChatRoom createRoom(String name){
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;


    }



}
