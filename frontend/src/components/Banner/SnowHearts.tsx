import React from 'react';
import styled, { keyframes } from 'styled-components';

const snowflakeAnimation = keyframes`
  0% {
    transform: translateY(-20vh) translateX(calc(100vw * var(--x)));
    opacity: 0;
  }
  100% {
    transform: translateY(80vh) translateX(calc(100vw * var(--x)));
    opacity: 1;
  }
`;

const Snowflake = styled.span<{ x: number; delay: number }>`
  position: absolute;
  font-size: 24px;
  color: red;
  animation: ${snowflakeAnimation} 6s forwards infinite;
  animation-fill-mode: both; // Retains both the start and end states
  animation-delay: ${({ delay }) => `${delay}s`};
  --x: ${({ x }) => x};
`;

const SnowHeart: React.FC<{ x: number; delay: number }> = ({ x, delay }) => {
  return (
    <Snowflake x={x} delay={delay}>
      &#10084;&#65039;
    </Snowflake>
  );
};

export default SnowHeart;
