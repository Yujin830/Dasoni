import { StreamManager } from 'openvidu-browser'; // HTML DOM에 미디어 스트림 나타내는 걸 담당
import { useEffect, useRef, useState } from 'react';

export const useStream = (streamManager: StreamManager) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [speaking, setSpeaking] = useState(false);
  const [micStatus, setMicStatus] = useState(streamManager.stream.audioActive);
  const [videoStatus, setVideoStatus] = useState(streamManager.stream.videoActive);

  useEffect(() => {
    streamManager.addVideoElement(videoRef.current as HTMLVideoElement);

    // publisher가 말하기 시작하면 이벤트 발생
    streamManager.on('publisherStartSpeaking', (e) => {
      // e.streamId : 이벤트가 발생한 stream의 id
      // streamManager.stream : publisher, subscriber가 있는 스트림
      // 이벤트가 발생한 stream이랑 현재 있는 stream이 다르면 return
      if (e.streamId !== streamManager.stream.streamId) return;
      setSpeaking(true);
    });

    // publisher가 말 멈추면 이벤트 발생
    streamManager.on('publisherStopSpeaking', (e) => {
      if (e.streamId !== streamManager.stream.streamId) return;
      setSpeaking(false);
    });

    // stream 속성이 변경됐을 때 이벤트 발생
    streamManager.on('streamPropertyChanged', (e) => {
      if (e.stream.streamId !== streamManager.stream.streamId) return;

      // changedProperty : 변경된 stream 속성
      // newValue : 변경된 새로운 속성 값
      if (e.changedProperty === 'videoActive') setVideoStatus(e.newValue as boolean);
      else if (e.changedProperty === 'audioActive') setMicStatus(e.newValue as boolean);
    });
  });

  return {
    speaking,
    micStatus,
    videoStatus,
    videoRef,
  };
};
