import React, { useState, useEffect, useRef } from 'react';
import { useWebSocket } from '../../hooks/useWebSocket';
import { useParams } from 'react-router';
import { useAppSelector } from '../../app/hooks';
import './ChatRoom.css';
import Loading01 from '../Loading/Loading';

interface ChatMessage {
  senderNickname: string;
  content: string;
  timestamp: Date;
  isUserMessage?: boolean;
}

const ChatRoom: React.FC = () => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const { roomId } = useParams();
  const messageRef = useRef<HTMLDivElement | null>(null);
  const member = useAppSelector((state) => state.user);
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태를 관리하는 상태 변수

  const client = useWebSocket({
    subscribe: (client) => {
      setIsLoading(false);
      console.log('로딩완료');

      // 기존의 채팅 메시지에 대한 구독
      client.subscribe(`/topic/room/${roomId}/chat`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        console.log(chatMessage);
        setMessages((messages) => [...messages, chatMessage]);
        console.log(messages);
      });
      // 입장 및 퇴장 메시지에 대한 구독 추가
      client.subscribe(`/topic/room/${roomId}`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        console.log(chatMessage);
        // 메시지 내용이 입장이나 퇴장 메시지인 경우에만 처리
        if (
          chatMessage.content.includes(`입장하셨습니다.`) ||
          chatMessage.content.includes(`퇴장하셨습니다.`)
        ) {
          setMessages((messages) => [...messages, chatMessage]);
        }
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
        `/app/room/${roomId}/chat`,
        {},
        JSON.stringify({
          senderNickname: member.nickname,
          content: newMessage,
          timestamp: new Date(),
          isUserMessage: true,
        }),
      );
      console.log(member);
      console.log(newMessage);
      setNewMessage('');
    }
  };
  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault(); // 새로고침을 막습니다
    sendMessage(); // 메시지를 보냅니다
  };

  return (
    <div className="chat-room">
      {/* 로딩 중일 때 스켈레톤 UI를 표시합니다. */}
      {isLoading ? (
        <Loading01 />
      ) : (
        // 로딩이 완료되면 실제 대기방 UI를 렌더링합니다.
        <>
          <div className="message-container" ref={messageRef}>
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`message ${member.nickname === msg.senderNickname ? 'message-own' : ''}`}
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
};

export default ChatRoom;
