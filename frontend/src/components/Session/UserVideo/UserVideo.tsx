import React, { useState } from 'react';
import OvVideo from '../OvVideo/OvVideo';
import { StreamManager } from 'openvidu-browser';
import './UserVideo.css';
import { useStream } from '../../../hooks/useStrem';
import { useAppSelector } from '../../../app/hooks';

interface UserVideoProps {
  streamManager: StreamManager;
  nickname: string;
  signalOpen: boolean;
}

// 유저의 화상 화면을 보여주는 컴포넌트
function UserVideo({ streamManager, nickname, signalOpen }: UserVideoProps) {
  const { videoRef, speaking } = useStream(streamManager);
  const { roomId, roomType } = useAppSelector((state) => state.waitingRoom);
  const { memberId } = useAppSelector((state) => state.user);
  const [isSendSignal, setIsSendSignal] = useState(false);

  const handleSendSignal = async () => {
    if (
      !isSendSignal &&
      confirm('한 번 선택하면 취소할 수 없습니다.\n이분에게 마음을 전하시겠습니까?')
    ) {
      const streamData = JSON.parse(streamManager.stream.connection.data);
      const data = {
        type: roomType,
        roomId: roomId,
        senderId: memberId,
        receiverId: streamData.memberId,
      };

      console.log('시그널 전송');
      console.log(data);

      // TODO : 서버로 시그널 데이터 전송

      setIsSendSignal(true);
    }
  };

  return (
    <OvVideo nickname={nickname} speaking={speaking}>
      <video className="stream-video" ref={videoRef}>
        <track kind="captions"></track>
      </video>
      {signalOpen && (
        <button className="meeting-signal" onClick={handleSendSignal}>
          <span className={`material-symbols-outlined ${isSendSignal ? 'sended' : ''}`}>
            favorite
          </span>
        </button>
      )}
    </OvVideo>
  );
}

export default UserVideo;
