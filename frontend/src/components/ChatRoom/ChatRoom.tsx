import React, { useState, useEffect, useRef } from 'react';
import { useWebSocket } from '../../hooks/useWebSocket';
import { useParams } from 'react-router';
import { useAppSelector } from '../../app/hooks';
import './ChatRoom.css';

interface ChatMessage {
  username: string;
  message: string;
  timestamp: Date;
}

const ChatRoom: React.FC = () => {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const { roomId } = useParams();
  const messageRef = useRef<HTMLDivElement | null>(null);
  const member = useAppSelector((state) => state.user);

  const client = useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}/chat`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        setMessages((messages) => [...messages, chatMessage]);
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
    if (newMessage) {
      client?.send(
        `/app/room/${roomId}/chat`,
        {},
        JSON.stringify({ username: 'senderNickname', message: newMessage, timestamp: new Date() }),
      );
      setNewMessage('');
    }
  };

  return (
    <div className="chat-room">
      <div className="message-container" ref={messageRef}>
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`message ${member.nickname === member?.nickname ? 'message-own' : ''}`}
          >
            <strong>{msg.username}:</strong> {msg.message}
          </div>
        ))}
      </div>
      <div className="chat-input">
        <input value={newMessage} onChange={(e) => setNewMessage(e.target.value)} />
        <button onClick={sendMessage}>보내기</button>
      </div>
    </div>
  );
};

export default ChatRoom;
