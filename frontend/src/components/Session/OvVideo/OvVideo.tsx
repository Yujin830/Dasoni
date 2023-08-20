import React from 'react';
import './OvVideo.css';

interface OvVideoProps {
  nickname: string;
  speaking: boolean;
  // micStatus: boolean;
  // videoStatus: boolean;
  children: React.ReactNode;
}

function OvVideo({ nickname, children, speaking }: OvVideoProps) {
  return (
    <div className="video-box">
      {children}
      <p className="video-name">{nickname}</p>
      <div className="speaking-box">
        <div className={`speaking-signal normal ${speaking ? 'speaking' : ''}`}></div>
        <div className={`speaking-signal large ${speaking ? 'speaking' : ''}`}></div>
        <div className={`speaking-signal normal ${speaking ? 'speaking' : ''}`}></div>
      </div>
    </div>
  );
}

export default OvVideo;
