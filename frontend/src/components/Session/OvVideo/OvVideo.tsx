import React from 'react';
import './OvVideo.css';

interface OvVideoProps {
  nickname: string;
  // speaking: boolean;
  // micStatus: boolean;
  // videoStatus: boolean;
  children: React.ReactNode;
}

function OvVideo({ nickname, children }: OvVideoProps) {
  return (
    <div className="video-box">
      {children}
      <p className="video-name">{nickname}</p>
    </div>
  );
}

export default OvVideo;
