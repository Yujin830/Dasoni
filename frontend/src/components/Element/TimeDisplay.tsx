import React, { useEffect, useState } from 'react';
import { CompatClient } from '@stomp/stompjs';
import './TimeDisplay.css';

interface TimeDisplayProps {
  client: CompatClient | undefined;
  roomId: string | undefined;
}

function TimeDisplay({ client, roomId }: TimeDisplayProps) {
  const [currentTime, setCurrentTime] = useState<string>('00:00');
  const startTime = new Date(); // 현재 시간을 시작 시간으로 설정

  useEffect(() => {
    const intervalId = setInterval(() => {
      const currentTime = new Date();
      const timeDiffInSeconds = Math.floor((currentTime.getTime() - startTime.getTime()) / 1000);

      updateTimer(timeDiffInSeconds);
    }, 1000);

    // 컴포넌트가 언마운트되면 인터벌 해제
    return () => clearInterval(intervalId);
  }, []);

  function updateTimer(timeInSeconds: number) {
    const minutes = Math.floor(timeInSeconds / 60)
      .toString()
      .padStart(2, '0');
    const seconds = (timeInSeconds % 60).toString().padStart(2, '0');
    // console.log(minutes, seconds);
    // 웹 소켓 메세지 sending
    if (minutes === '05' && seconds === '00') client?.send(`/app/room/${roomId}/guide`, {}, '5');
    else if (minutes === '20' && seconds === '00')
      client?.send(`/app/room/${roomId}/guide`, {}, '20');
    else if (minutes === '50' && seconds === '00')
      client?.send(`/app/room/${roomId}/guide`, {}, '50');
    // 시그널 메세지 send
    else if (minutes === '00' && seconds === '25') client?.send(`/app/room/${roomId}/signal`);

    setCurrentTime(`${minutes}:${seconds}`);
  }

  return (
    <div className="time-display">
      <div className="time">{currentTime}</div>
    </div>
  );
}

export default TimeDisplay;
