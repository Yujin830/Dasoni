import React from 'react';
import OvVideo from '../OvVideo/OvVideo';
import { StreamManager } from 'openvidu-browser';
import './UserVideo.css';
import { useStream } from '../../../hooks/useStrem';

interface UserVideoProps {
  streamManager: StreamManager;
  nickname: string;
}

// 유저의 화상 화면을 보여주는 컴포넌트
function UserVideo({ streamManager, nickname }: UserVideoProps) {
  const { videoRef, speaking } = useStream(streamManager);

  return (
    <OvVideo nickname={nickname} speaking={speaking}>
      <video className="stream-video" ref={videoRef}>
        <track kind="captions"></track>
      </video>
    </OvVideo>
  );
}

export default UserVideo;
