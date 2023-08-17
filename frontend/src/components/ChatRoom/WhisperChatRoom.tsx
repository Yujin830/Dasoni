import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router';
import { useAppSelector } from '../../app/hooks';
import './WhisperChatRoom.css';
import { useWebSocket } from '../../hooks/useWebSocket';
import Loading01 from '../Loading/Loading';

interface ChatMessage {
  senderNickname: '';
  content: string;
  timestamp: Date;
  isUserMessage?: boolean;
  receiverId: string;
}

interface WhisperChatRoomProps {
  diffGenderMemberList: any[];
}

function WhisperChatRoom({ diffGenderMemberList }: WhisperChatRoomProps) {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [newMessage, setNewMessage] = useState('');
  const [whisperTarget, setWhisperTarget] = useState<any>();
  const [chatDisabled, setChatDisabled] = useState(false);
  const { roomId } = useParams<{ roomId: string }>();
  const { memberId } = useAppSelector((state) => state.user);
  const messageRef = useRef<HTMLDivElement | null>(null);
  const member = useAppSelector((state) => state.user);
  const [isLoading, setIsLoading] = useState(false);

  const client = useWebSocket({
    subscribe: (client) => {
      client.subscribe(`/topic/room/${roomId}/chat`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        // setIsLoading(false); 귓속말 웹소켓 연결되는 타이밍이 언젠지 못잡겠음
        setMessages((messages) => [...messages, chatMessage]);
      });

      client.subscribe(`/topic/room/${roomId}/whisper/${memberId}`, (res: any) => {
        const chatMessage: ChatMessage = JSON.parse(res.body);
        console.log('chatmessage', chatMessage);
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
    console.log('whispertarget', whisperTarget);
    if (newMessage && client?.connected) {
      client.send(
        `/app/room/${roomId}/whisper/${whisperTarget}`,
        {},
        JSON.stringify({
          memberId: memberId,
          senderNickname: '익명',
          content: newMessage,
          timestamp: new Date(),
          isUserMessage: true,
          receiverId: whisperTarget,
        }),
      );

      setNewMessage('');
      setWhisperTarget(null);

      if (whisperTarget) {
        setChatDisabled(true);
        setMessages((prevMessages) => [
          ...prevMessages,
          {
            senderNickname: '',
            content: '상대방에게 마음이 전달되었어요.',
            timestamp: new Date(),
            receiverId: whisperTarget,
          },
        ]);
      }
    }
  };

  const handleFormSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!whisperTarget) {
      alert('귓속말 대상을 선택해주세요!');
      return;
    }
    sendMessage();
  };

  return (
    <div className="chat-room">
      {/* 로딩 중일 때 스켈레톤 UI를 표시합니다. */}
      {isLoading ? (
        <Loading01 />
      ) : (
        <>
          <div className="message-container" ref={messageRef}>
            {messages.map((msg, index) => (
              <div
                key={index}
                className={`message ${member.nickname === msg.senderNickname ? 'message-own' : ''}`}
              >
                <strong>{msg.senderNickname}</strong>
                {msg.content !== '상대방에게 마음이 전달되었어요.'
                  ? `누군가 : ${msg.content}`
                  : ` ${msg.content}`}
              </div>
            ))}
          </div>

          <form onSubmit={handleFormSubmit} className="chat-input">
            <select
              className="diff-member-selector"
              value={whisperTarget || ''}
              onChange={(e) => setWhisperTarget(e.target.value || null)}
            >
              <option value="">귓속말 대상 선택</option>
              {diffGenderMemberList.map((member, index) => (
                <option key={index} value={member.memberId}>
                  {member.nickname}
                </option>
              ))}
            </select>
            <input
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              disabled={chatDisabled}
            />
            <button type="submit" disabled={chatDisabled || !whisperTarget}>
              보내기
            </button>
          </form>
        </>
      )}
    </div>
  );
}

export default WhisperChatRoom;
