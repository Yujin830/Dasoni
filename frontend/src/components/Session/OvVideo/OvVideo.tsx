import React, { useRef, useEffect } from 'react';
import { StreamManager } from 'openvidu-browser';
import './OvVideo.css';

type OvVideoProps = {
  streamManager: StreamManager;
};

function OvVideo({ streamManager }: OvVideoProps) {
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return (
    <video className="ov-video" autoPlay={true} ref={videoRef}>
      <track kind="captions"></track>
    </video>
  );
}

export default OvVideo;
