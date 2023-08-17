import React, { useEffect } from 'react';
import './TimeDisplay.css';

interface TimeDisplayProps {
  currentTime: string;
  startSec: string | number;
  setCurrentTime: (time: string) => void;
}

const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
function TimeDisplay({ currentTime, startSec, setCurrentTime }: TimeDisplayProps) {
  useEffect(() => {
    const intervalId = setInterval(() => {
      const currentTime = new Date();
      const timeDiffInSeconds = Math.floor(
        (currentTime.getTime() - (Number(startSec) + KR_TIME_DIFF)) / 1000,
      );
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
    setCurrentTime(`${minutes}:${seconds}`);
  }

  return (
    <div className="time-display">
      <div className="time">{currentTime}</div>
    </div>
  );
}

export default TimeDisplay;
