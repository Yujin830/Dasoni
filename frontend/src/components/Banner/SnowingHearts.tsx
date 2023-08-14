import React, { useEffect, useState } from 'react';
import SnowHeart from './SnowHearts';

const SnowingHearts: React.FC = () => {
  const [snowflakes, setSnowflakes] = useState<JSX.Element[]>([]);

  useEffect(() => {
    const snowflakeInterval = setInterval(() => {
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
    }, 12000);

    return () => clearInterval(snowflakeInterval);
  }, []);

  return <>{snowflakes}</>;
};

export default SnowingHearts;
