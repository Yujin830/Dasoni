import React, { useState, useRef, useEffect } from 'react';
import Loading01 from '../../Loading/Loading';
import { useAppSelector } from '../../../app/hooks';
import { useWebSocket } from '../../../hooks/useWebSocket';
import './MatchChatRoom.css';

interface ChatMessage {
  memberId: number | undefined;
  receiverId: number;
  senderNickname: string | undefined;
  content: string;
}

interface MatchChatRoomProps {
  chattingMemberId: number;
  setSendBtn: (state: any) => void;
}

function MatchChatRoom({ chattingMemberId, setSendBtn }: MatchChatRoomProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태를 관리하는 상태 변수
  const messageRef = useRef<HTMLDivElement | null>(null);
  const member = useAppSelector((state) => state.user);

  const client = useWebSocket({
    subscribe: (client) => {
      setIsLoading(false);
      console.log('로딩완료');

      // 기존의 채팅 메시지에 대한 구독
      client.subscribe(`/queue/mypage/chat/${member.memberId}/${chattingMemberId}`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        console.log(chatMessage);
        setMessages((messages) => [...messages, chatMessage]);
        console.log(messages);
      });
    },
  });

  useEffect(() => {
    if (messageRef && messageRef.current) {
      const element = messageRef.current;
      element.scroll({
        top: element.scrollHeight,
        left: 0,
        behavior: 'smooth',
      });
    }
  }, [messages]);

  const sendMessage = () => {
    if (newMessage && client?.connected) {
      console.log(client.connected);
      client.send(
        `/app/mypage/chat/${chattingMemberId}`,
        {},
        JSON.stringify({
          memberId: member.memberId,
          receiverId: chattingMemberId,
          senderNickname: member.nickname,
          content: newMessage,
        }),
      );
      console.log(member);
      console.log(newMessage);
      setNewMessage('');
    }
  };

  // 메시지 전송
  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 새로고침을 막습니다
    sendMessage(); // 메시지를 보냅니다
  };

  // 채팅창 닫기
  const handleCloseChatting = () => {
    setSendBtn(false);
  };

  return (
    <div id="match-chat-room" className="chat-room">
      {/* 로딩 중일 때 스켈레톤 UI를 표시합니다. */}
      {isLoading ? (
        <Loading01 />
      ) : (
        // 로딩이 완료되면 실제 대기방 UI를 렌더링합니다.
        <>
          <div className="message-container" ref={messageRef}>
            <button className="close-btn" onClick={handleCloseChatting}>
              <span className="material-symbols-outlined">cancel</span>
            </button>
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`message ${
                  member.nickname === msg.senderNickname ? 'message-own' : 'message-other'
                }`}
              >
                {msg.content.includes(`입장하셨습니다.`) ||
                msg.content.includes(`퇴장하셨습니다.`) ? (
                  <>
                    <strong>{msg.senderNickname} </strong> {msg.content}
                  </>
                ) : (
                  <>
                    <strong>{msg.senderNickname} : </strong> {msg.content}
                  </>
                )}
              </div>
            ))}
          </div>
        </>
      )}
      <form onSubmit={handleFormSubmit} className="chat-input">
        <input value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
        <button type="submit">보내기</button>
      </form>
    </div>
  );
}

export default MatchChatRoom;
