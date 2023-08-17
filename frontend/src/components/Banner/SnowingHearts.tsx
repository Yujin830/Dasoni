import React, { useEffect, useState } from 'react';
import SnowHeart from './SnowHearts';

const SnowingHearts: React.FC = () => {
  const [snowflakes, setSnowflakes] = useState<JSX.Element[]>([]);

  useEffect(() => {
    // 애니메이션 시작 함수 정의
    const startAnimation = () => {
      const numFlakes = Math.floor(Math.random() * 15) + 15;
      const newSnowflakes: JSX.Element[] = [];

      for (let i = 0; i < numFlakes; i++) {
        const xPosition = Math.random() * 2 - 1;
        const delay = Math.random() * 6;
        const newSnowflake = (
          <SnowHeart key={newSnowflakes.length + i} x={xPosition} delay={delay} />
        );
        newSnowflakes.push(newSnowflake);
      }

      setSnowflakes(newSnowflakes);
    };

    // 페이지 로드시 애니메이션 시작
    startAnimation();

    // 12초마다 애니메이션 갱신
    const snowflakeInterval = setInterval(startAnimation, 12000);

    return () => clearInterval(snowflakeInterval);
  }, []);

  return <>{snowflakes}</>;
};

export default SnowingHearts;
