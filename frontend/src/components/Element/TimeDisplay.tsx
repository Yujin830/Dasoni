import React, { useEffect } from 'react';
import './TimeDisplay.css';

interface TimeDisplayProps {
  currentTime: string;
  startSec: string;
  setCurrentTime: (time: string) => void;
}

function TimeDisplay({ currentTime, startSec, setCurrentTime }: TimeDisplayProps) {
  // const startTime = new Date(); // 현재 시간을 시작 시간으로 설정
  // console.log('startTime ', startTime.getTime());

  console.log(`startSec ${startSec}`);
  useEffect(() => {
    const intervalId = setInterval(() => {
      const currentTime = new Date();
      const timeDiffInSeconds = Math.floor((currentTime.getTime() - Number(startSec)) / 1000);
      // const timeDiffInSeconds = Math.floor((currentTime.getTime() - startTime.getTime()) / 1000);

      updateTimer(timeDiffInSeconds);
    }, 1000);

    // 컴포넌트가 언마운트되면 인터벌 해제
    return () => clearInterval(intervalId);
  }, [startSec]);

  function updateTimer(timeInSeconds: number) {
    // console.log('timeInSeconds', timeInSeconds);
    const minutes = Math.floor(timeInSeconds / 60)
      .toString()
      .padStart(2, '0');
    const seconds = (timeInSeconds % 60).toString().padStart(2, '0');
    // console.log(`${minutes}:${seconds}`);
    setCurrentTime(`${minutes}:${seconds}`);
  }

  return (
    <div className="time-display">
      <div className="time">{currentTime}</div>
    </div>
  );
}

export default TimeDisplay;
